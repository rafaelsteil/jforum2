jf=../../
java -Djava.ext.dirs=$jf/lib:$jf/WEB-INF/lib -cp ../bin:$jf/WEB-INF/classes net.jforum.tools.search.LuceneCommandLineReindexer $@