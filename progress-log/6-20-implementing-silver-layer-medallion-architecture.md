A key insight here is that, between transformation steps, we materialize the state after the transformation.
Curiously, basically every mapreduce function in the hadoop era persists it's output in HDFS. That way, we can chain many mapreduce functions using output from earlier steps as input.
Today we can make it using medallion architecture, by taking raw, low quality data (labeled as bronze), and enrich, clean, transform and store each step in another storage, 
labelling the data better (silver and gold).
The object here is make the data the best as possible.
