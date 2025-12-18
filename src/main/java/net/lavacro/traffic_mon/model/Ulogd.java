package net.lavacro.traffic_mon.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
public class Ulogd {
	private OffsetDateTime timestamp;

	@JsonProperty("dvc")
	private String device;

	@JsonProperty("pktlen")
	private int packetLen;

	@JsonProperty("pktcount")
	private int packetCount;

//	"oob.prefix": "INTERNAL",
//	"oob.time.sec": 1761535133,
//	"oob.time.usec": 266635,
//	"oob.mark": 0,
//	"oob.ifindex_in": 2,
//	"oob.hook": 1,
//	"raw.mac_len": 14,
//	"oob.family": 2,
//	"oob.protocol": 2048,
//	"raw.label": 0,
//	"raw.type": 1,
//	"raw.mac.addrlen": 6,

	@JsonProperty("ip.protocol")
	private Integer ipProtocol;

//	"ip.tos": 16,
//	"ip.ttl": 64,
//	"ip.totlen": 52,
//	"ip.ihl": 5,
//	"ip.csum": 20535,
//	"ip.id": 26405,
//	"ip.fragoff": 16384,

	@JsonProperty("src_port")
	private Integer srcPort;

	@JsonProperty("dest_port")
	private Integer destPort;

//	"tcp.seq": 2957051646,
//	"tcp.ackseq": 1002120773,
//	"tcp.window": 16151,
//	"tcp.offset": 0,
//	"tcp.reserved": 0,
//	"tcp.urg": 0,
//	"tcp.ack": 1,
//	"tcp.psh": 0,
//	"tcp.rst": 0,
//	"tcp.syn": 0,
//	"tcp.fin": 0,
//	"tcp.res1": 0,
//	"tcp.res2": 0,
//	"tcp.csum": 9673,

	@JsonProperty("src_ip")
	private String srcIp;

	@JsonProperty("dest_ip")
	private String destIp;
}
