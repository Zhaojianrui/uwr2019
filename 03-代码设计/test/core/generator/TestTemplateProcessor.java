package core.generator;
import core.common.*;
import org.easymock.EasyMock;
import org.easymock.IAnswer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(PowerMockRunner.class)
@PrepareForTest(DataSourceConfig.class)
@PowerMockIgnore("javax.management.*")
public class TestTemplateProcessor implements DataSourceType{
	//寰呮祴璇曠被(SUT)鐨勪竴涓疄渚嬨��
	private TemplateProcessor tp;
	//渚濊禆绫�(DOC)鐨勪竴涓疄渚嬨��
	private DataSourceConfig dsc;

	@Test
	public void testStaticVarExtract() throws Exception {

		//璁剧疆寰呮祴璇曠被鐨勭姸鎬侊紙娴嬭瘯鐩爣鏂规硶锛�
		tp.staticVarExtract("resource/newtemplatezzz.doc");
		//浠ヤ笅杩涜妫�鏌ョ偣璁剧疆
		DataSource ds = dsc.getConstDataSource();

		List<DataHolder> dhs = ds.getVars();
		DataHolder dh1 = ds.getDataHolder("sex");
		assertNotNull("鍙橀噺sex瑙ｆ瀽涓虹┖", dh1);
		assertEquals("鍙橀噺sex鍊艰幏鍙栭敊璇�","Female",dh1.getValue());

		DataHolder dh2 = ds.getDataHolder("readme");
		assertNotNull("鍙橀噺readme瑙ｆ瀽涓虹┖", dh2);
		assertEquals("鍙橀噺readme鍊艰幏鍙栭敊璇�","5",dh2.getValue());

		DataHolder dh3 = ds.getDataHolder("testexpr");
		assertNotNull("鍙橀噺testexpr", dh3);
		assertEquals("鍙橀噺testexpr鐨勮〃杈惧紡瑙ｆ瀽閿欒","${num}+${readme}",dh3.getExpr());
		dh3.fillValue();
		assertEquals("鍙橀噺testexpr","5.0",dh3.getValue());

		//妫�娴婼UT鐨勫疄闄呰涓烘ā寮忔槸鍚︾鍚堥鏈�
		PowerMock.verifyAll();
	}

	@Before
	public void setUp() throws Exception {

		//浠ヤ笅閲囩敤Mock瀵硅薄鐨勬柟寮忥紝鍋氭祴璇曞墠鐨勫噯澶囥��
		//涓庝互涓婃柟娉曟瘮杈冿紝濂藉鏄檷浣嶴UT锛圱emplateProcessor绫伙級涓嶥OC锛圖ataSourceConfig绫伙級涔嬮棿鐨勮�﹀悎鎬э紝瑙ｈ�﹀畠浠��
		//浠庤�屼娇寰楀畾浣嶇己闄峰彉寰楀鏄撱��
		//鍙傜収娴佺▼锛�
		//1. 浣跨敤EasyMock寤虹珛涓�涓狣ataSourceConfig绫荤殑涓�涓狹ock瀵硅薄瀹炰緥锛�
		//2. 褰曞埗璇ュ疄渚嬬殑STUB妯″紡鍜岃涓烘ā寮忥紙閽堝鐨勬槸闈為潤鎬佹柟娉曪級锛�
		//3. 浣跨敤PowerMock寤虹珛DataSourceConfig绫荤殑闈欐�丮ock锛�
		//4. 褰曞埗璇ラ潤鎬丮ock鐨勮涓烘ā寮忥紙閽堝鐨勬槸闈欐�佹柟娉曪級锛�
        //------------------------------------------------
        //浠ヤ笂娴佺▼璇峰湪杩欓噷瀹炵幇锛�
        //
        //
        // 杩欓噷鍐欎唬鐮�
        //
        //------------------------------------------------
		//5. 閲嶆斁鎵�鏈夌殑琛屼负銆�
		DataHolder dhsex = EasyMock.createMock(DataHolder.class);
		dhsex.setName("sex");
		EasyMock.expect(dhsex.getValue()).andStubReturn("Female");
		DataHolder dhrm = EasyMock.createMock(DataHolder.class);
		dhrm.setName("readme");
		EasyMock.expect(dhrm.getValue()).andStubReturn("5");
		DataHolder dhte = EasyMock.createMock(DataHolder.class);
		dhte.setName("testexpr");
		EasyMock.expect(dhte.getExpr()).andStubReturn("${num}+${readme}");
		EasyMock.expect(dhte.fillValue()).andStubReturn(null);
		EasyMock.expect(dhte.getValue()).andStubReturn("5.0");
		//vars涓篊onstDataSource锛圖ataSource锛夋垚鍛�
		ArrayList<DataHolder> vars=new ArrayList<>();
		//DataHolder鏀惧叆ArrayList<DataHolder>
		vars.add(dhsex);
		vars.add(dhrm);
		vars.add(dhte);



		//重放录制内容，ConstDataSource与DataHolder
		EasyMock.replay(cds);
		EasyMock.replay(dhsex, dhrm, dhte);
		//Powermock静态mock行为，测试静态方法
		PowerMock.mockStatic(DataSourceConfig.class);
		EasyMock.expect(DataSourceConfig.newInstance()).andStubReturn(dsc);
		PowerMock.replayAll(dsc);
		//鍒濆鍖栦竴涓緟娴嬭瘯绫伙紙SUT锛夌殑瀹炰緥
		tp = new TemplateProcessor();
	}
}
