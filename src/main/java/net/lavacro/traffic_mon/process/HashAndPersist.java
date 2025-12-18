package net.lavacro.traffic_mon.process;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.*;
import lombok.extern.slf4j.Slf4j;
import net.lavacro.traffic_mon.config.MongoConfig;
import net.lavacro.traffic_mon.model.Ulogd;
import net.openhft.hashing.LongTupleHashFunction;
import org.bson.BsonBinarySubType;
import org.bson.Document;
import org.bson.types.Binary;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

@Service
@Slf4j
public class HashAndPersist {
	private final MongoCollection<Document> collection;

	private final LongTupleHashFunction xxh128 = LongTupleHashFunction.xx128();

	public HashAndPersist(MongoClient mongoClient, MongoConfig mongoConfig) {
		collection = mongoClient.getDatabase(mongoConfig.getDatabase()).getCollection(mongoConfig.getCollection());
	}

	public Callable<Object> hashAndStore(List<Ulogd> items) {
		return () -> {
			List<WriteModel<Document>> ops = new ArrayList<>(items.size());
			// Build the key string used for hashing
			for(Ulogd data : items) {
				if(data == null) {
					log.error("Data is null !!!");
					continue;
				}
				if(data.getSrcIp().equals(data.getDestIp())) {
					// reflected rule
					continue;
				}
				String str = String.format("%s|%s|%d|%d",
						data.getSrcIp(), data.getDestIp(), data.getDestPort(), data.getIpProtocol());
				long[] hash = xxh128.hashBytes(str.getBytes(StandardCharsets.UTF_8));

				// Convert the 128-bit hash (two longs) into 16 bytes for MongoDB _id
				byte[] hashBytes = ByteBuffer.allocate(16)
						.order(ByteOrder.BIG_ENDIAN)
						.putLong(hash[0])
						.putLong(hash[1])
						.array();
				Binary id = new Binary(BsonBinarySubType.BINARY, hashBytes);

				// all sources are internal and resolvable, so let's find hostnames ...
				InetAddress addr = InetAddress.getByName(data.getSrcIp());

				UpdateOneModel<Document> update = new UpdateOneModel<>(
						Filters.eq("_id", id),
						Updates.combine(
								Updates.inc("count", 1),
								Updates.setOnInsert("sourceIP", data.getSrcIp()),
								Updates.setOnInsert("destIP", data.getDestIp()),
								Updates.setOnInsert("protocol", data.getIpProtocol()),
								Updates.setOnInsert("port", data.getDestPort()),
								Updates.setOnInsert("firstSeen", data.getTimestamp().toInstant()),
								Updates.set("lastSeen", data.getTimestamp().toInstant()),
								Updates.setOnInsert("sourceHost", addr.getHostName())
						),
						new UpdateOptions().upsert(true)
				);

				ops.add(update);
			}

			return collection.bulkWrite(ops, new BulkWriteOptions().ordered(false));
		};
	}
}
