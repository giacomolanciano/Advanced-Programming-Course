import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.ToolRunner;



public class NodePairDistance {

	static int printUsage() {
		System.out.println("NodePairDistance [-r <reduces>] <input> <output>");
		ToolRunner.printGenericCommandUsage(System.out);
		return -1;
	}

	public static void main(String[] args) throws Exception {
		
		List<String> otherArgs = new ArrayList<String>();

		Configuration conf = new Configuration();
		
		for(int i=0; i < args.length; ++i) {
			try {
				if ("-r".equals(args[i])) { 
                    //to customize the number of reducers
					conf.setInt("mapreduce.job.reduces", Integer.parseInt(args[++i]));
				} else {
					otherArgs.add(args[i]);
				}
			} catch (NumberFormatException except) {
				System.out.println("ERROR: Integer expected instead of " + args[i]);
				System.exit(printUsage());
			} catch (ArrayIndexOutOfBoundsException except) {
				System.out.println("ERROR: Required parameter missing from " +
						args[i-1]);
				System.exit(printUsage());
			}
		}

		// Make sure there are exactly 2 parameters left.
		if (otherArgs.size() != 2) {
			System.out.println("ERROR: Wrong number of parameters: " +
					otherArgs.size() + " instead of 2.");
			System.exit(printUsage());
		}
		
        //take input and output folders from command line
		Path input = new Path(otherArgs.get(0));
		Path output =new Path(otherArgs.get(1));
		
		Job job = Job.getInstance(conf);
        job.setJarByClass(NodePairDistance.class);
        job.setJobName("NodePairDistance");
        
	    FileInputFormat.addInputPath(job, input);
	    FileOutputFormat.setOutputPath(job, output);

	    job.setMapperClass(MyMapper.class);
	    //job.setCombinerClass(MyReducer.class);
	    job.setReducerClass(MyReducer.class);

        // An InputFormat for plain text files. 
        // Files are broken into lines. Either linefeed or carriage-return are used 
        // to signal end of line. Keys are the position in the file, and values 
        // are the line of text.
	    job.setInputFormatClass(TextInputFormat.class);

        //to specify the types of intermediate result key and value
        job.setMapOutputValueClass(EdgeWritable.class);     //it is the custom type defined below
        
        //to specify the types of output key and value
	    job.setOutputKeyClass(Text.class);
	    job.setOutputValueClass(Text.class);

	    job.waitForCompletion(true);
	}
	
    
	public static class MyMapper extends Mapper<LongWritable, Text, Text, EdgeWritable>{
		
		@Override
		protected void map(LongWritable key, Text value, Context context)
				throws IOException, InterruptedException {
                    
            String edge = value.toString();   
            String[] tok = edge.split(" ");
            String start = tok[0];
            String end = tok[1];
            int startInt = Integer.parseInt(start);
            int endInt = Integer.parseInt(end);
            
			context.write(new Text(start),                         //a
                            new EdgeWritable(startInt, endInt));   //(a b)
                            
            context.write(new Text(end),                           //b
                            new EdgeWritable(startInt, endInt));   //(a b)
			
       	}
	}
	
	public static class MyReducer extends Reducer<Text, EdgeWritable, Text, Text>{

		@Override
		protected void reduce(Text key, Iterable<EdgeWritable> values, Context context)
				throws IOException, InterruptedException {
            
            List<Integer> S = new ArrayList<Integer>();
            List<Integer> E = new ArrayList<Integer>();
            
            Iterator<EdgeWritable> it = values.iterator();
            
            /* 
             * necessary to verify equality between node and key.
             * without declaring it outside the loop, output seems empty */ 
            String keyString = key.toString();
            int keyInt = Integer.parseInt(keyString);
            
            while (it.hasNext()) {
                EdgeWritable v = it.next();
                int start = v.getX();
                int end = v.getY();
                
                if (start == keyInt)
                    E.add(end);
                else 
                    S.add(start);
            }
            
            for (Integer a : S)
                for(Integer b : E)
                    context.write(new Text(a.toString()), new Text(b.toString()));
            
			
		}
	}
    
    public static class EdgeWritable implements Writable {
        
        private int x, y;
        
        public EdgeWritable(){
             /*
              * NECESSARY, defining a non-empty constructor we "lose" the default one.
              * when it is called by the reducer a NoSuchMethodException arise,
              * if not defined */
             x = 0;
             y = 0;
        }
        
        public EdgeWritable(int a, int b) {
            x = a;
            y = b;
        }
        
        @Override
        public void readFields(DataInput in) throws IOException {
            x = in.readInt();
            y = in.readInt();
        }
        
        @Override
        public void write(DataOutput out) throws IOException {
            out.writeInt(x);
            out.writeInt(y);
        }
        
        public int getX() {
            return x;
        }
        
        public int getY() {
            return y;
        }
        
    }
}
