package net.lavacro.traffic_mon.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.afterburner.AfterburnerModule;
import net.lavacro.traffic_mon.TrafficMonException;
import net.lavacro.traffic_mon.model.Ulogd;
import org.apache.kafka.common.serialization.Deserializer;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.Map;

public class UlogdDeserializer implements Deserializer<Ulogd> {

	private final ObjectReader ulogdReader; // prebuilt reader for performance
	private final Base64.Decoder base64 = Base64.getDecoder(); // cached decoder

	public UlogdDeserializer() {
		ObjectMapper objectMmapper = new ObjectMapper();
		objectMmapper.registerModule(new JavaTimeModule());
		objectMmapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		objectMmapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		// Optional but useful for speed:
		objectMmapper.registerModule(new AfterburnerModule());
		ulogdReader = objectMmapper.readerFor(Ulogd.class);
	}

	public UlogdDeserializer(ObjectMapper objectMapper) {
		objectMapper.registerModule(new JavaTimeModule());
		objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		objectMapper.registerModule(new AfterburnerModule());
		ulogdReader = objectMapper.readerFor(Ulogd.class);
	}

	@Override
	public void configure(Map<String, ?> configs, boolean isKey) {
		// we don't use keys, so ignore
	}

	@Override
	public Ulogd deserialize(String topic, byte[] data) {
		if (data == null) {
			return null;
		}

		final boolean quoted = data.length > 2 && data[0] == '"'; // assume trailing quote
		final int offset = quoted ? 1 : 0;
		final int len = quoted ? data.length - 2 : data.length;

		try(
			InputStream is = new ByteArrayInputStream(data, offset, len);
			InputStream decoded = base64.wrap(is)
		) {
			return ulogdReader.readValue(decoded);
		} catch(IOException e) {
			throw new TrafficMonException("Deserialization error", e);
		}

	}

	@Override
	public void close() {
		// nothing to close
	}
}