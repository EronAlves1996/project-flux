Platform: LinkedIn or Twitter/X

Hook: Day 3/20: Implementing the Ingestion Layer with Batch Patterns. 📥
Body:
Built IngestionService to decouple data generation from processing.
Implemented a "Pull" pattern to collect micro-batches of transactions.
Refined currency generation to use BigDecimal.valueOf(double) for simulation-safe precision.
Takeaway: Ingestion isn't just moving data; it's about defining boundaries and batch sizes for downstream efficiency.
Hashtags/Tags: #DataEngineering #Java #BatchProcessing #SystemDesign

Continuing on the project, I fixed the value generation to guarantee real values generation, compreend fractionary values.
In conjunction with that, I implemented a logic using batch pattern, where my system pulls data from the source system using batches.
