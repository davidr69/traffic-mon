# Traffic Monitor (traffic-mon)



```javascript
db.createCollection("allTraffic", {
    validator: {
        $jsonSchema: {
            bsonType: "object",
            required: ["_id"],
            properties: {
                _id: {
                    bsonType: "binData",
                    description: "Must be a 16-byte binary value (XXH128)"
                },
                sourceIP: { bsonType: "string" },
                sourceHost: { bsonType: "string" },
                destIP: { bsonType: "string" },
                protocol: { bsonType: "int" },
                port: { bsonType: "int" },
                firstSeen: { bsonType: "date" },
                lastSeen: { bsonType: "date" },
                count: { bsonType: "int" }
            }
        }
    },
    validationLevel: "strict",
    validationAction: "error"
});
```

```javascript
db.allTraffic.insertOne({
    _id: new BinData(0, 'D4AtlJhg9vlO6shQxBE8sA=='),
    sourceIP: '192.168.1.42',
    destIP: '8.8.8.8',
    protocol: 17,
    port: 53,
    lastSeen: ISODate('2025-10-26T22:32:59.023664-0400'),
    firstSeen: ISODate('2025-10-26T22:32:59.023664-0400'),
    sourceHost: 'nube.lavacro.net',
    count: 1
});
```
