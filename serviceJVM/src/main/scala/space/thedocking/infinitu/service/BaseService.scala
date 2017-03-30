package space.thedocking.infinitu.service

import org.apache.curator.framework.CuratorFrameworkFactory
import com.typesafe.scalalogging.Logger
import net.ceedubs.ficus.Ficus._
import org.apache.curator.retry.RetryForever
import com.typesafe.config.ConfigFactory
import com.typesafe.config.Config
import org.apache.curator.retry.ExponentialBackoffRetry

import org.apache.curator.utils.CloseableUtils
import org.apache.curator.framework.CuratorFramework
import org.apache.curator.x.discovery.ServiceDiscovery
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder
import org.apache.curator.x.discovery.ServiceInstance
import org.apache.curator.x.discovery.UriSpec
import org.apache.curator.x.discovery.details.JsonInstanceSerializer
import java.io.Closeable
import java.io.IOException

import scala.collection.JavaConverters._

class BaseService(val details: InstanceDetails) extends Closeable {

  val config: Config = ConfigFactory.load

  val retryPolicy = new ExponentialBackoffRetry(5 * 1000, 3)

  lazy val zk = CuratorFrameworkFactory.newClient(config.as[String]("zookeeper.connectionString"), retryPolicy)

  lazy val serviceInstance: ServiceInstance[InstanceDetails] = {
    ServiceInstance.builder[InstanceDetails]()
      .name(details.serviceName)
      .payload(details)
      .port(details.servicePort)
      .uriSpec(details.uriSpec)
      .build()
  }

  lazy val serviceDiscovery: ServiceDiscovery[InstanceDetails] = {
    ServiceDiscoveryBuilder.builder(classOf[InstanceDetails])
      .client(zk)
      .basePath(details.servicePath)
      .serializer(BaseService.serializer)
      .thisInstance(serviceInstance)
      .build()
  }
  
  def start = serviceDiscovery.start
  
  override def close = CloseableUtils.closeQuietly(serviceDiscovery)

}

object BaseService {

  val logger = Logger[BaseService]

  val serializer = new JsonInstanceSerializer[InstanceDetails](classOf[InstanceDetails])

  def main(args: Array[String]): Unit = {
    val srv = new BaseService(InstanceDetails( 
    serviceName= "test", 
    serviceHost = "localhost", 
    servicePort = 9876, 
    servicePath = "/discovery/test", 
    serviceDescription = "testing service discovery"))
    logger.info(s"Zookeeper connection string: ${srv.zk.getZookeeperClient.getCurrentConnectionString}")
    srv.zk.start
    logger.info(s"Children: ${srv.zk.getChildren.forPath("/")}")

    srv.start
    logger.info(srv.serviceDiscovery.queryForInstances(srv.details.serviceName).asScala.map { instance =>
      s"${instance.getPayload.serviceDescription}: ${instance.buildUriSpec}"
    }.mkString(", "))
    srv.close

    srv.zk.close
  }

}
