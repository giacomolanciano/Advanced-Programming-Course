import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.input.KeyValueTextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.ToolRunner;



public class ClusteringCoefficient {
    
    private static final String JOB_NAME = "ClusteringCoefficient";
    private static final String MAP_TAG_1 = "[mapper1] ";
    private static final String MAP_TAG_2 = "[mapper2] ";
    private static final String MAP_TAG_3 = "[mapper3] ";
    private static final String RED_TAG_1 = "[reducer1] ";
    private static final String RED_TAG_2 = "[reducer2] ";
    private static final String RED_TAG_3 = "[reducer3] ";
    private static final String TEMP_DIR_1 = "/outClustTemp1";
    private static final String TEMP_DIR_2 = "/outClustTemp2";

	static int printUsage() {
		System.out.println(JOB_NAME + " [-r <reducers>] <input> <output>");
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
        
        //tmp folder for first pass output
        Path tmpOut_1 = new Path(TEMP_DIR_1);
        
        //tmp folder for second pass output
        Path tmpOut_2 = new Path(TEMP_DIR_2);
		
		
        /*
         * BEGIN OF FIRST PASS
         * */
        
        Job job = Job.getInstance(conf);
        job.setJarByClass(ClusteringCoefficient.class);
        job.setJobName(JOB_NAME);
        
	    FileInputFormat.addInputPath(job, input);
	    FileOutputFormat.setOutputPath(job, tmpOut_1);

	    job.setMapperClass(MyMapper.class);
	    //job.setCombinerClass(MyReducer.class);
	    job.setReducerClass(MyReducer.class);

    
	    job.setInputFormatClass(KeyValueTextInputFormat.class);
        
        //to specify the types of intermediate result key and value
        job.setMapOutputKeyClass(IntWritable.class);
        job.setMapOutputValueClass(IntWritable.class);

        //to specify the types of output key and value
	    job.setOutputKeyClass(Text.class);
	    job.setOutputValueClass(Text.class);

	    job.waitForCompletion(true);
        
        
        
        /*
         * BEGIN OF SECOND PASS
         * */
         
        job = Job.getInstance(conf);
        job.setJarByClass(ClusteringCoefficient.class);
        job.setJobName(JOB_NAME);
        
	    FileInputFormat.addInputPath(job, tmpOut_1);
	    FileOutputFormat.setOutputPath(job, tmpOut_2);

	    job.setMapperClass(MyMapper2.class);
	    //job.setCombinerClass(MyReducer.class);
	    job.setReducerClass(MyReducer2.class);

        
	    job.setInputFormatClass(TextInputFormat.class);
        

        //to specify the types of output key and value
	    job.setOutputKeyClass(Text.class);
	    job.setOutputValueClass(Text.class);

	    job.waitForCompletion(true);
        
        
        /*
         * BEGIN OF THIRD PASS
         * */
		
        job = Job.getInstance(conf);
        job.setJarByClass(ClusteringCoefficient.class);
        job.setJobName(JOB_NAME);
        
	    FileInputFormat.addInputPath(job, tmpOut_2);
	    FileOutputFormat.setOutputPath(job, output);

	    job.setMapperClass(MyMapper3.class);
	    //job.setCombinerClass(MyReducer.class);
	    job.setReducerClass(MyReducer3.class);

	    job.setInputFormatClass(TextInputFormat.class);
	    
	    //to specify the types of intermediate result key and value
        job.setMapOutputKeyClass(IntWritable.class);
        job.setMapOutputValueClass(Text.class);
        
        //to specify the types of output key and value
	    job.setOutputKeyClass(Text.class);
	    job.setOutputValueClass(Text.class);

	    job.waitForCompletion(true);
		
    }

	
	public static class MyMapper extends Mapper<Text, Text, IntWritable, IntWritable> {

		private IntWritable z = new IntWritable();
		private IntWritable w = new IntWritable();
		int xi, yi;

		@Override
		protected void map(Text x, Text y, Context context)
				throws IOException, InterruptedException {
					
			/*
			 * identity function
			 *  
			 * It would be not necessary to define a mapper here, since
			 * identity function is the default. 
			 * Although, with this implementation a smaller amount of bytes 
			 * is passed from the mapper to the reducer. Since it represents 
			 * the bottleneck of MapReduce, performances are improved.
			 * 
			 * NOTE: the undirected graph in input has to be represented
			 * as a list of edges s.t. if u,v is in the list ALSO v,u has
			 * to occur
			 * */
			
			xi = Integer.parseInt(x.toString());
			yi = Integer.parseInt(y.toString());
			z.set(xi);        // boxing
			w.set(yi);        // boxing
			context.write(z, w);
		}
	}

	public static class MyReducer extends Reducer<IntWritable, IntWritable, Text, Text> {

		private String out;
		private Text xt = new Text();
		private Text yt = new Text();

		@Override
		protected void reduce(IntWritable node, Iterable<IntWritable> neighbours, Context context)
						throws IOException, InterruptedException {
			
			/*
			 * compute the neighbours list of node
			 * */
			
			out="";
 
			for (IntWritable x: neighbours)
				out += x +"\t";
            
            xt.set(node.toString());
            yt.set(out.trim());
            context.write(xt, yt);
		}
	}
	
	public static class MyMapper2 extends Mapper<LongWritable,Text,Text,Text> {
		private Text outKey = new Text();
		private Text outValue = new Text();
		String line, node;   
		String[] tok;

		@Override
		protected void map(LongWritable x, Text y, Context context)
				throws IOException, InterruptedException {
            
            /*
             * "send" the neighbours list of the node to each of its
             * neighbours, in order to discover connections among them
             * 
             * NOTE: a copy of the list is "sent" to the node itself
             * */
            
            line = y.toString();   
            tok = line.split("\t");
            node = tok[0];
            
            for(int i = 1; i < tok.length; i++) {
                outKey.set(tok[i]);
                outValue.set(line);
                context.write(outKey, outValue);
                
                //DEBUG
                //System.out.println(MAP_TAG_2 + "key = " + outKey + "\tvalue = " + outValue);
            }
            
            outKey.set(node);
            outValue.set(line);
			context.write(outKey, outValue);
			
			//DEBUG
            //System.out.println(MAP_TAG_2 + "key = " + outKey + "\tvalue = " + outValue);
		}
	}

	public static class MyReducer2 extends Reducer<Text,Text,Text,Text> {
        
        private ArrayList<String> val = new ArrayList<String>();
        private ArrayList<String> keyNeighbours = new ArrayList<String>();
        private ArrayList<String> intersection = new ArrayList<String>();
		private Text outKey = new Text();
		private Text outValue = new Text();
        private String aux;   
        private String[] tok;
        private int keyInt;

		@Override
		protected void reduce(Text key, Iterable<Text> values, Context context)
				throws IOException, InterruptedException {
			
            val.clear();
            keyNeighbours.clear();
            intersection.clear();
            
            keyInt = Integer.parseInt(key.toString());
                        
            for(Text z : values) {
                val.add(z.toString());
            }
            
            /*
             * retreive the neighbours list of the current node (key)
             * */
            for(String z : val) {
                tok = z.split("\t");
                if(tok[0].equals(key.toString())) {
                    for(int i = 1; i < tok.length; i++) {
                        keyNeighbours.add(new String(tok[i]));
                    }
                }
            }
            
            //DEBUG
            //System.out.println(RED_TAG_2 + "neighbours(" + key.toString() + ") = " + keyNeighbours);
            
            
            /*
             * for each neighbour, compute the intersection among current
             * node list and neighbour list, to see whether there are 
             * connections in the neighbourhood
             * */
            for(String z : val) {
				
				intersection.clear();
                
                tok = z.split("\t");
                
                if(!tok[0].equals(key.toString())) {
                    
                    
                    /*
                     * compute intersection
                     * */
                    for(int i = 1; i < tok.length; i++) {
                        for(String w : keyNeighbours)
							if(w.equals(tok[i]))
								intersection.add(new String(tok[i]));
                    }
                    
                    //DEBUG
					//System.out.println(RED_TAG_2 + "intersection(" + key.toString() + ", " + tok[0] + ") = " + intersection);
                    
                    outKey.set(tok[0]);
                    outValue.set((tok.length -1) + "\t" + intersection.size());
                    context.write(outKey, outValue);
                }
            }
            
		}
	}
    
    public static class MyMapper3 extends Mapper<LongWritable,Text,IntWritable,Text> {
		
		private IntWritable outKey = new IntWritable();
		private Text outValue = new Text();
		String line;
		String[] tok;
		int node;

		@Override
		protected void map(LongWritable x, Text y, Context context)
				throws IOException, InterruptedException {
					
			/*
			 * identity function
			 * 
			 * It would be not necessary to define a mapper here, since
			 * identity function is the default. 
			 * Although, with this implementation a smaller amount of bytes 
			 * is passed from the mapper to the reducer. Since it represents 
			 * the bottleneck of MapReduce, performances are improved.
			 * 
			 * */
                    
            line = y.toString();
            
            //DEBUG
			//System.out.println(MAP_TAG_3 + "input = " + line);   
            
            tok = line.split("\t");
            node = Integer.parseInt(tok[0]);
            
            line = "";
            for(int i = 1; i < tok.length; i++) {
                line += tok[i] + "\t";
            }
            line.trim();
            
			outKey.set(node);
            outValue.set(line);
			context.write(outKey, outValue);
			
			//DEBUG
			//System.out.println(MAP_TAG_3 + "output = " + node + "\t" + line);
		}
	}

	public static class MyReducer3 extends Reducer<IntWritable,Text,Text,Text> {
		
		/*
		 * NOTE: in this refined implementation, output is sorted according
		 * to a numerical order (not lexicographical anymore).
		 * To compare two outputs sorted in different ways, type on terminal:
		 * 
		 * $ sort out1.txt | sort out2.txt | diff - -
		 * 
		 * */
        
        private ArrayList<String> val = new ArrayList<String>();
		private Text outKey = new Text(), outValue = new Text();
        private int neighbours, num;
        private long den;
        private double clusteringCoefficient;
        private boolean neighboursRetreived, found;
        private String[] tok, edgeTok;
        

		@Override
		protected void reduce(IntWritable key, Iterable<Text> values, Context context)
				throws IOException, InterruptedException {
                    
            val.clear();
            num = 0;
            
            for(Text z : values) {
                val.add(z.toString());
            }
            
            neighboursRetreived = false;
            for(String x : val) {
                tok = x.split("\t");
                
				/*
				 * retreive the size of the neighbourhood (only once)
				 * */
                if(!neighboursRetreived) {
                    neighbours = Integer.parseInt(tok[0]);
                    neighboursRetreived = true;
                }
                
                /*
                 * compute the set of connections in neighbourhood
                 * (with duplicates)
                 * */                
                num += Integer.parseInt(tok[1]);
                
            }
            
            
            /*
             * compute clustering coefficient
             * */
             
            //DEBUG
			//System.out.println(RED_TAG_3 + "union(" + key.toString() + ") = " + union);
            
            /*
             * division by 2 not to count duplicates
             * */
            num /= 2;
            
            //DEBUG
			//System.out.println(RED_TAG_3 + "num = #connections among" + key.toString() + "neighbours = " + num);
			
            den = binomial(neighbours, 2);
            
            //DEBUG
			//System.out.println(RED_TAG_3 + "den = " + neighbours + " chooses 2 = " + den);
			
            clusteringCoefficient = (double) num / (double) den;
            
            //DEBUG
			//System.out.println(RED_TAG_3 + "clusteringCoefficient = " + clusteringCoefficient);
            
            outKey.set(key.toString());
            outValue.set(String.format("%.2f", clusteringCoefficient));
            context.write(outKey, outValue);
            	
		}
        
        
        /*
         * utility function to compute "n chooses k"
         * */
        private static long binomial(int n, int k) {
            if (k>n-k)
                k=n-k;
     
            long b=1;
            for (int i=1, m=n; i<=k; i++, m--)
                b=b*m/i;
            return b;
        }
        
	}
}
