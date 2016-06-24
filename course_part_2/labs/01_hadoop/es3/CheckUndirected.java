import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import java.util.ListIterator;

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
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.ToolRunner;


public class CheckUndirected {

	static int printUsage() {
		System.out.println("CheckUndirected [-r <reduces>] <input> <output>");
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
		job.setJarByClass(CheckUndirected.class);
		job.setJobName("CheckUndirected");
		
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

		//tells the system the types of output key and value
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);

		job.waitForCompletion(true);
	}
	
	
	public static class MyMapper extends Mapper<LongWritable, Text, Text, Text>{
		
		@Override
		protected void map(LongWritable key, Text value, Context context)
				throws IOException, InterruptedException {
					
			String edge = value.toString();   
			String[] tok = edge.split(" ");
			String start = tok[0];
			String end = tok[1];
			
			context.write(new Text(start),	 //a
							new Text(edge));   //(a b)
							
			context.write(new Text(end),	   //b
							new Text(edge));   //(a b)
			
	   	}
	}
	
	public static class MyReducer extends Reducer<Text, Text, Text, Text>{

		@Override
		protected void reduce(Text key, Iterable<Text> values, Context context)
				throws IOException, InterruptedException {
			
			List<String> S = new ArrayList<String>();
			List<String> E = new ArrayList<String>();
			
			Iterator<Text> it = values.iterator();
			
			//necessario per verificare uguaglianza tra nodo e chiave
			//senza dichiarare la variabile prima l'output risulta vuoto 
			String keyString = key.toString();
			
			while (it.hasNext()) {
				String v = it.next().toString();
				String[] tok = v.split(" ");
				
				if (tok[0].equals(keyString))
					E.add(tok[1]);
				else 
					S.add(tok[0]);
			}
			

			/*
			 * SOLUZIONE CON RIMOZIONE ELEMENTI DALLE LISTE
			 * 
			 * questa soluzione è costosa (O(n^2)) poichè non è possibile rimuovere
			 * elementi dalla lista mentre la si scorre senza generare eccezione,
			 * quindi è necessario iterare su una copia ed eliminare dall'originale.
			 * Tentato di utilizzare ListIterator.remove() senza successo
			 * (alla fine gli elementi sembrano rimanere comunque nella lista).
			 * 
			 * Tuttavia in questo modo si riescono a stampare precisamente 
			 * gli eventuali edge che non hanno una controparte 
			 * (cioè che rendono il grafo diretto)*/
						

			for (String a : new ArrayList<String>(S)) {
				for (String b : new ArrayList<String>(E)) {
					if(a.equals(b)) {
						S.remove(a);
						E.remove(b);
						break;
					}
				}
			}
			
			
			
			if(!S.isEmpty())
				for (String a : S)
					context.write(new Text(a), new Text(keyString));
					
			if(!E.isEmpty())
				for (String b : E)
					context.write(new Text(keyString), new Text(b));		
			
			
			
			
			/*
			 * SOLUZIONE SENZA RIMOZIONE ELEMENTI DALLE LISTE
			 * 
			 * meno costosa della precedente, ma non c'è il modo di stampare
			 * precisamente gli edge che rendono il grafo diretto.*/			
			
			/*
			ListIterator<String> itS = S.listIterator();
			ListIterator<String> itE = E.listIterator();
			String auxS, auxE;
			boolean equalFound = false;

			while (itS.hasNext()) {
				auxS = itS.next();
				while (itE.hasNext()) {
					auxE = itE.next();
					if(auxS.equals(auxE)) {
						equalFound = true;
						break;
					}
				}
				if(!equalFound) {
					context.write(new Text(keyString), 
									new Text("an edge starting/ending in this node does not have a counterpart"));
					break;
				}
			}
			*/
			
			
		}
	}
}
