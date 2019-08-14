 第一版：
   从文本中获取人名、地名、组织。
 
 使用方法：
   1.从https://stanfordnlp.github.io/CoreNLP/index.html#download下载中文模型和Stanford CoreNLP工程。
   2.将中文模型放入工程目录中。
   3.使用命令启动java -mx4g -cp "*;stanford-corenlp-full-2019-10-08/*" edu.stanford.nlp.pipeline.StanfordCoreNLPServer -port 9000 -timeout 15000，在服务器上9000端口启动了一个接口程序。
   4.将本工程编译打包，放入plugins/steps（kettle加载步骤插件的扫描的四个目录之一，也可放在例如c盘USER/.kettle/plugins下）下。
   5.启动kettle即可。
