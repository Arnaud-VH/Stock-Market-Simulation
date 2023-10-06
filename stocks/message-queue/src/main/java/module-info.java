module messagequeue {
    exports nl.rug.aoop.messagequeue to networking;
    requires static lombok;
    requires com.google.gson;
    requires org.slf4j;
}