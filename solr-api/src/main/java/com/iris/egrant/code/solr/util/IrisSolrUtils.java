package com.iris.egrant.code.solr.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.ObjectUtils;
import org.apache.solr.client.solrj.impl.HttpSolrClient;

import com.iris.egrant.code.solr.model.SolrItem;
import com.iris.egrant.core.cp.model.CompareListInfo;

public class IrisSolrUtils {

	//指定solr服务器的地址  
    private String SOLR_URL;  
    //指定solr的核心
    private String SOLR_CORE="cores";
    
    private HttpSolrClient solrClient = null;
    
    private ThreadLocal<HttpSolrClient> solrClientLocal  = new ThreadLocal<HttpSolrClient>();
    
    //防止solrClient打开和关闭的方法发生并发问题
    //private volatile boolean isLock = false;
    
    /**
     * 单例初始化
     */
    private static IrisSolrUtils instance;
    private IrisSolrUtils (){
    	
    	String runEnv = System.getProperty("spring.profiles.active", "run");
		InputStream inStream = this.getClass().getResourceAsStream("/config/system."+runEnv+".properties");
		Properties prps = new Properties();
		try {
			prps.load(inStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		SOLR_URL = prps.getProperty("solr_url");
    	
    	//SOLR_URL = "http://dev.egrant3.egrant.cn/egrantsolr/";
    	
    }  
    public static IrisSolrUtils getIrisSolrUtilsInstance() {  
		if (instance == null) {  
		    instance = new IrisSolrUtils();
		}
		return instance; 
    }
    
    /**
     * 初始化HttpSolrClient（如果多线程可能会出现问题）
     * @throws Exception
     */
    public void InitSolrClient() throws Exception{
    	InitSolrClient(SOLR_CORE);
    }
    
    /**
     * 初始化HttpSolrClient（如果多线程可能会出现问题）
     * @param url solr服务器的地址  
     * @param core  solr服务器的核心
     * @throws Exception
     */
	public synchronized  void InitSolrClient(String core) throws Exception{
		//while(isLock==true);
		//isLock=true;
    	solrClient = new HttpSolrClient.Builder(SOLR_URL+core+"/").build();
    }
    
	public HttpSolrClient InitSolrClientLocal(String core){
		HttpSolrClient httpSolrClient = solrClientLocal.get();
		if(httpSolrClient==null){
			httpSolrClient = new HttpSolrClient.Builder(SOLR_URL+core+"/").build();
			solrClientLocal.set(httpSolrClient);
		}
		return httpSolrClient;
	}
	
	public HttpSolrClient getSolrClientLocal(){
		HttpSolrClient httpSolrClient = solrClientLocal.get();
		return httpSolrClient;
	}
	
	public void CloseSolrClientLocal() throws Exception{
		HttpSolrClient httpSolrClient = solrClientLocal.get();
		if(httpSolrClient!=null){
			httpSolrClient.close();
		}
		solrClientLocal.set(null);
	}
	
    public HttpSolrClient getSolrClient() {
		return solrClient;
	}
	public void setSolrClient(HttpSolrClient solrClient) {
		this.solrClient = solrClient;
	}
	/**
     * 关闭HttpSolrClient
     * @throws Exception
     */
    public void CloseSolrClient() throws Exception{
    	if(solrClient!=null){
    		 solrClient.close();
    		 //isLock=false;
    	}
    }
   
    
    /**
	 * 基于Bean增加分词索引
	 * @param item
	 */
    public void addDocumentBean(SolrItem item) throws Exception{
    	getSolrClientLocal().addBean(item);
    	//solrClient.optimize();
    	getSolrClientLocal().commit();
    }
    
    
    
    /**
	 * 基于Bean列表增加分词索引
	 * @param itemList
	 */
    public void addDocumentSolrItemBeans(List<SolrItem> itemList) throws Exception{
    	getSolrClientLocal().addBeans(itemList);
    	//solrClient.optimize();
    	getSolrClientLocal().commit();
    }
    
    
    /**
     * 删除所有分词索引
     * @throws Exception
     */
    public void delDocumentAll() throws Exception{
    	getSolrClientLocal().deleteByQuery("*:*");
    	getSolrClientLocal().commit();
    }
    
    /**
     * 删除指定keyCode和type分词索引
     * @throws Exception
     */
    public void delDocumentBean(String keyCode , String type) throws Exception{
    	getSolrClientLocal().deleteByQuery("id:"+keyCode+" AND type:"+type);
    	getSolrClientLocal().commit();
    }
    
    
    /**
     * 通过index删除指定分词索引
     * @param index
     * @throws Exception
     */
    public void delDocumentById(String index) throws Exception{
    	getSolrClientLocal().deleteById(index);
    	getSolrClientLocal().commit();
    }
    
    /**
     * 优化查询性能
     * @param index
     * @throws Exception
     */
    public void optimize() throws Exception{
    	getSolrClientLocal().optimize();
    }
    
    /**
     * 测试
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
    	
    	//IrisSolrUtils irisSolrUtils = IrisSolrUtils.getIrisSolrUtilsInstance();
    	//irisSolrUtils.toRun();
    	
    	String testStr = "*.2?5+2$^5["
    			+ " ](12 ){}|12";
    	
    	//Pattern p = Pattern.compile("\\s*|\t|\r|\n");
    	Pattern p = Pattern.compile("[*.?+$^\\[\\](){}|\\s*|\t|\r|\n]");
		Matcher m = p.matcher(testStr);
		String after = m.replaceAll("");
		System.out.println(testStr);
        System.out.println(after);
    }
    
    public void toRun() throws Exception {
    	 ExecutorService fixedThreadPool = Executors.newFixedThreadPool(1);
    	 
         (new Thread(){   
             public void run(){     
            	int i=0;
             	while(i<0){
             		try {
 						Thread.sleep(100);
 						
 	            		fixedThreadPool.execute(new ExecuteTask());
 	            		
 	            		System.out.println("跑第"+i+"次");
 	            		
 	            		i++;
 					} catch (InterruptedException e) {
 						e.printStackTrace();
 					}
             		//fixedThreadPool.execute(new ExecuteTask());
             	}
             }  
         }).start(); 
		
	}
    
    
    protected class ExecuteTask implements Runnable {

		@Override
		public void run() {
			
			System.out.println(Thread.currentThread().getName()+"开始");
	    	
	    	CompareListInfo compareListInfo = new CompareListInfo();
	    	compareListInfo.setId(1l);
	    	compareListInfo.setDataType(2);
	    	compareListInfo.setKeyCode(1l);
	    	compareListInfo.setContent("你是我的眼");
	    	compareListInfo.setType(1);
	    	
	    	IrisSolrUtils irisSolrUtils = IrisSolrUtils.getIrisSolrUtilsInstance();
	    	try {
	    		
	    		irisSolrUtils.InitSolrClientLocal("solr_cores_executableReport");
		    	
		    	
		    	irisSolrUtils.delDocumentBean(ObjectUtils.toString(compareListInfo.getId()),ObjectUtils.toString(compareListInfo.getType()));
		    	SolrItem solrItem = new SolrItem();;
				solrItem.setId(ObjectUtils.toString(compareListInfo.getId()));
				solrItem.setContent(compareListInfo.getContent());
				solrItem.setType(ObjectUtils.toString(compareListInfo.getType()));
				solrItem.setSystype("wxfms");
				
				irisSolrUtils.addDocumentBean(solrItem);
	    		
	    	} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					irisSolrUtils.CloseSolrClientLocal();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
	    	
	        
	        System.out.println(Thread.currentThread().getName()+"结束");
			
		}
    	
    	
   }
}
