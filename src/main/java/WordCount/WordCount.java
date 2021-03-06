package WordCount;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/**
 * 相当于一个yarn集群的客户端
 * 需要在此封装我们的mr程序的相关运行参数，指定jar包
 * 最后提交给yarn
 * @author
 *
 */
public class WordCount {

    public static void main(String[] args) throws Exception {
            String[] paths = new String[2];
            //paths[0] = "hdfs://127.0.0.1:9000//input/word.txt";
            //paths[1] = "hdfs://127.0.0.1:9000/output/output15";
            paths[0] = "./input/wordcount_input";
            paths[1] = "./output/wordcountoutput";


        Configuration conf = new Configuration();

        //设置的没有用!  ??????
//		conf.set("HADOOP_USER_NAME", "hadoop");
//		conf.set("dfs.permissions.enabled", "false");


		/*conf.set("mapreduce.framework.name", "yarn");
		conf.set("yarn.resoucemanager.hostname", "mini1");*/
        Job job = Job.getInstance(conf);

        /*job.setJar("/home/hadoop/wc.jar");*/
        //指定本程序的jar包所在的本地路径
        job.setJarByClass(WordCount.class);

        //指定本业务job要使用的mapper/Reducer业务类
        job.setMapperClass(WordcountMapper.class);
        job.setReducerClass(WordcountReducer.class);

        //指定mapper输出数据的kv类型
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);

        //指定最终输出的数据的kv类型
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);




        //指定需要使用combiner，以及用哪个类作为combiner的逻辑
        /*job.setCombinerClass(WordcountCombiner.class);*/
        job.setCombinerClass(WordcountReducer.class);



        //指定job的输入原始文件所在目录
        FileInputFormat.setInputPaths(job, new Path(paths[0]));
        //指定job的输出结果所在目录
        FileOutputFormat.setOutputPath(job, new Path(paths[1]));

        //将job中配置的相关参数，以及job所用的java类所在的jar包，提交给yarn去运行
        /*job.submit();*/
        boolean res = job.waitForCompletion(true);
        System.exit(res?0:1);

    }


}
