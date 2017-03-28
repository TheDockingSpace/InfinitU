package space.thedocking.infinitu.service

import org.apache.curator.framework.CuratorFrameworkFactory
import com.typesafe.scalalogging.Logger
import net.ceedubs.ficus.Ficus._
import org.apache.curator.retry.RetryForever
import com.typesafe.config.ConfigFactory
import com.typesafe.config.Config
import org.apache.curator.retry.ExponentialBackoffRetry

class BaseService {

}

object BaseService {
  
  val logger = Logger[BaseService]
  
  val config: Config = ConfigFactory.load
  
  val retryPolicy = new ExponentialBackoffRetry(5 * 1000, 3)
  
  val zk = CuratorFrameworkFactory.newClient(config.as[String]("zookeeper.connectionString"), retryPolicy)
    
  def main(args: Array[String]): Unit = {
    logger.info(s"Zookeeper connection string: ${zk.getZookeeperClient.getCurrentConnectionString}")
    zk.start
    logger.info(s"Children: ${zk.getChildren.forPath("/")}")
    zk.close
  }

}
