import javax.naming.spi.DirStateFactory.Result;

import org.apache.http.impl.client.FutureRequestExecutionMetrics;
import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TJSONProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TMemoryBuffer;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.CorsHandler;
import tutorial.Calculator;
import tutorial.CalculatorHandler;

public class VertxThriftTest extends AbstractVerticle{
	public static void main(String[] args) {
		Vertx vertx=Vertx.vertx();
		Router router = Router.router(vertx);
		//类::方法示例
//		vertx.createHttpServer().requestHandler(resquest->{
//			router.accept(resquest);
//		});
		// 解决跨域问题
		router.route().handler(
				CorsHandler.create("*").allowedMethod(HttpMethod.GET)
						.allowedMethod(HttpMethod.POST)
						.allowedMethod(HttpMethod.OPTIONS)
						.allowedHeader("X-PINGARUNER")
						.allowedHeader("Content-Type"));
		router.route("/server/*").handler(context->{
			context.request().handler(buffer->{
				byte[] arr=buffer.getBytes();
				vertx.executeBlocking(future->{
					String result=thriftRequest(arr);
					future.complete(result);
				}, res->{
					if(res.succeeded()) {
						context.response().end(res.result().toString());
					}else {
						context.response().end(res.cause().getMessage());
					}
				});
			});
		});
		vertx.createHttpServer().requestHandler(router::accept).listen(8088, res->{
			if(res.succeeded()) {
				System.out.println("Server starts successfully!");
			}else {
				System.out.println("Server fails to start!");
			}
		});
		vertx.deployVerticle(new VertxThriftTest());
	}
	private static String thriftRequest(byte[] input){
        try{
        
            //Input
            TMemoryBuffer inbuffer = new TMemoryBuffer(input.length);           
            inbuffer.write(input);              
            TProtocol  inprotocol   = new TJSONProtocol(inbuffer);                   
            
            //Output
            TMemoryBuffer outbuffer = new TMemoryBuffer(100);           
            TProtocol outprotocol   = new TJSONProtocol(outbuffer);
            
            TProcessor processor = new Calculator.Processor(new CalculatorHandler());      
            processor.process(inprotocol, outprotocol);
            
            byte[] output = new byte[outbuffer.length()];
            outbuffer.readAll(output, 0, output.length);
        
            return new String(output,"UTF-8");
        }catch(Throwable t){
            return "Error:"+t.getMessage();
        }
         
                 
    }
}

