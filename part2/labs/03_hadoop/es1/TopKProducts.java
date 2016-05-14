import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Iterator;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.ToolRunner;



public class TopKProducts {
    
    private static final String JOB_NAME = "TopKProducts";
    private static final String MAP_TAG = "[mapper] ";
    private static final String RED_TAG = "[reducer] ";
    private static final String TEMP_DIR = "/outTopKProductsTemp";
    private static final int K_PRODUCTS = 5;

	static int printUsage() {
		System.out.println(JOB_NAME + " [-r <reduces>] <input> <output>");
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
        Path tmpOut = new Path(TEMP_DIR);
		
		Job job = Job.getInstance(conf);
        job.setJarByClass(TopKProducts.class);
        job.setJobName(JOB_NAME);
        
	    FileInputFormat.addInputPath(job, input);
	    FileOutputFormat.setOutputPath(job, tmpOut);

	    job.setMapperClass(MyMapper.class);
	    //job.setCombinerClass(MyReducer.class);
	    job.setReducerClass(MyReducer.class);

        // An InputFormat for plain text files. 
        // Files are broken into lines. Either linefeed or carriage-return are used 
        // to signal end of line. Keys are the position in the file, and values 
        // are the line of text.
	    job.setInputFormatClass(TextInputFormat.class);
        
        //to specify the types of intermediate result key and value
        job.setMapOutputKeyClass(ProductPairWritable.class);
        job.setMapOutputValueClass(IntWritable.class);

        //to specify the types of output key and value
	    job.setOutputKeyClass(Text.class);
	    job.setOutputValueClass(Text.class);

	    job.waitForCompletion(true);
        
        
        /*
         * BEGIN OF SECOND PASS
         * */
         
        job = Job.getInstance(conf);
        job.setJarByClass(TopKProducts.class);
        job.setJobName(JOB_NAME);
        
	    FileInputFormat.addInputPath(job, tmpOut);
	    FileOutputFormat.setOutputPath(job, output);

	    job.setMapperClass(MyMapperKProducts.class);
	    //job.setCombinerClass(MyReducer.class);
	    job.setReducerClass(MyReducerKProducts.class);

        /* 
         * An InputFormat for plain text files.
         * Lines are borken in tokens, using space as delimiters */
	    job.setInputFormatClass(TextInputFormat.class);
        
        //to specify the types of intermediate result key and value
        job.setMapOutputKeyClass(IntWritable.class);
        job.setMapOutputValueClass(Text.class);

        //to specify the types of output key and value
	    job.setOutputKeyClass(Text.class);
	    job.setOutputValueClass(Text.class);

	    job.waitForCompletion(true);
		
		
    }
	
    
	public static class MyMapper extends Mapper<LongWritable, Text, ProductPairWritable, IntWritable>{
		
        private ProductPairWritable pair = new ProductPairWritable();
        private IntWritable one = new IntWritable(1);
        private List<String> products = new ArrayList<String>();
        private String line, userId;
        private String[] tok;
        
		@Override
		protected void map(LongWritable key, Text value, Context context)
				throws IOException, InterruptedException {
            
            /*
             * clean common data structures
             * */
            products.clear();
                    
            line = value.toString();   
            tok = line.split(" ");
            userId = tok[0];
            
            /*
             * create a copy of the list of products, to compute cross product
             * */
            for(int i = 1; i < tok.length; i++) {
                products.add(tok[i]);
            }
            
            for(String prod : products) {
                for(int i = 1; i < tok.length; i++) {
                    if(!prod.equals(tok[i])) {
                        pair.setP1(Integer.parseInt(prod));
                        pair.setP2(Integer.parseInt(tok[i]));
                        
                        context.write(pair, one);
                    }
                }
            }			
       	}
	}
	
	public static class MyReducer extends Reducer<ProductPairWritable, IntWritable, Text, Text>{

        private int sum;
        private Text outKey = new Text(), outValue = new Text();
        
		@Override
		protected void reduce(ProductPairWritable key, Iterable<IntWritable> values, Context context)
				throws IOException, InterruptedException {
            
            sum = 0;
            for(IntWritable i : values)
                sum += 1;
            
            outKey.set(key.getP1()+"");
            outValue.set(key.getP2() + "\t" + sum);
            
            context.write(outKey, outValue);
		}
	}
    
    public static class MyMapperKProducts extends Mapper<LongWritable,Text,IntWritable,Text>{
        
        private IntWritable outKey = new IntWritable();
        private Text outValue = new Text();
        private String line, p1, p2Sum;
        private String[] tok;
		
		@Override
		protected void map(LongWritable key, Text value, Context context)
				throws IOException, InterruptedException {
                    
            line = value.toString();   
            tok = line.split("\t");
            p1 = tok[0];
            p2Sum = tok[1] + "\t" + tok[2];
            
            outKey.set(Integer.parseInt(p1));
            outValue.set(p2Sum);
            
            context.write(outKey, outValue);
            
            //DEBUG
            System.out.println(MAP_TAG + "outkey: " + outKey + ", outValue: " + outValue);
			
       	}
	}
	
	public static class MyReducerKProducts extends Reducer<IntWritable,Text,IntWritable,Text>{
        
        private ArrayList<Text> list = new ArrayList<Text>();
        private Text outValue = new Text();
        private String line, topKProducts;
        private String[] tok;

		@Override
		protected void reduce(IntWritable key, Iterable<Text> values, Context context)
				throws IOException, InterruptedException {
            /*
             * clean common data structures
             * */
            list.clear();
            
            /*
             * copy list of values to sort it
             * */
            Iterator<Text> it = values.iterator();
            Text aux;
            while(it.hasNext()) {
                aux = it.next();
                
                /*
                 * it is NECESSARY to create a new Text obj to add to list
                 * */
                list.add(new Text(aux.toString()));
            }
                
            //DEBUG
            System.out.println(RED_TAG + "list before sorting: " + list);
            
            /*
             * sort list of values in descending order by sum
             * */
            Collections.sort(list, new Comparator<Text>() {
                public int compare(Text o1, Text o2) {
                    String line;
                    String[] tok;
                    Long sum1, sum2;
                    
                    line = o1.toString();   
                    tok = line.split("\t");
                    sum1 = Long.parseLong(tok[1]);
                    
                    line = o2.toString();   
                    tok = line.split("\t");
                    sum2 = Long.parseLong(tok[1]);
                    
                    return sum2.compareTo(sum1);
                }
            });
            
            //DEBUG
            System.out.println(RED_TAG + "list after sorting: " + list);
            
            /*
             * returns only the top K products
             * */
            topKProducts = "";
            int i = 0;
            for(Text t : list) {                
                line = t.toString();   
                tok = line.split("\t");
                topKProducts += tok[0] + "\t";
                
                i++;
                if(i >= K_PRODUCTS) {
                    
                    //DEBUG
                    System.out.println(RED_TAG + "break, i = " + i);
                    
                    break;
                }
            }
            
            outValue.set(topKProducts.trim());
            context.write(key, outValue);
		}
	}
    
    public static class ProductPairWritable implements WritableComparable<ProductPairWritable> {
        
        private int p1, p2;
        
        public ProductPairWritable(){
             /*
              * NECESSARY, defining a non-empty constructor we "lose" the default one.
              * when it is called by the reducer a NoSuchMethodException arise,
              * if not defined 
              * */
             
             p1 = 0;
             p2 = 0;
        }
        
        public ProductPairWritable(int a, int b) {
            p1 = a;
            p2 = b;
        }
        
        @Override
        public void readFields(DataInput in) throws IOException {
            p1 = in.readInt();
            p2 = in.readInt();
        }
        
        @Override
        public void write(DataOutput out) throws IOException {
            out.writeInt(p1);
            out.writeInt(p2);
        }
        
        @Override
        public int compareTo(ProductPairWritable o) {
            if(this.p1 < o.getP1()) return -1;
            if(this.p1 > o.getP1()) return 1;
            return new Integer(this.p2).compareTo(o.getP2());
        }
        
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            ProductPairWritable pair = (ProductPairWritable) o;

            boolean direct = true, inverse = true;
            
            if (getP1() != pair.getP1()) direct = false;
            if (getP2() != pair.getP2()) direct = false;
            
            if (getP1() != pair.getP2()) inverse = false;
            if (getP2() != pair.getP1()) inverse = false;
            
            return direct || inverse;

        }
        
        public int getP1() {
            return p1;
        }
        
        public int getP2() {
            return p2;
        }
        
        public void setP1(int p1) {
            this.p1 = p1;
        }
        
        public void setP2(int p2) {
            this.p2 = p2;
        }
        
    }
    
}
