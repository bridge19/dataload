package user;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.xy.logload.service.FileDownloadService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:config/applicationContext-main.xml" })
public class BaseTest extends AbstractJUnit4SpringContextTests{

	@Autowired private FileDownloadService fileDownloadService;
	
	@Test
	public void testDownLoadFile() {
		fileDownloadService.getFileList("lujingGis","out.log","20180411");
	}

	@Test
	public void testPrintFile(){
//		fileDownloadService.readFile("out.log-20180411_10.33.33.186.gz");
	}
	
	@Test
	public void readRemoteFileTest(){
		fileDownloadService.readRemoteFile("out.log-20180411_10.33.33.186.gz");
	}
}