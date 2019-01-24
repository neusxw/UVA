package test;

import java.io.File;
import java.util.List;

import main.arithmetic.CreateTrajectoryByGA;
import main.arithmetic.DistributeGrid;
import main.arithmetic.data.CoordinateTransformation;
import main.arithmetic.data.DataExport;
import main.arithmetic.data.MapInfo;
import main.arithmetic.data.ReadXMLByDom4j;
import main.entity.Grid;
import main.entity.Map;
import main.entity.PolygonFactory;
import main.entity.UAV;

public class OneUAVXmlTest {

	public static void main(String[] args) {
		//DataExport.changeOutPosition();
		DataExport dataExport = new DataExport();
		File file = new File("rs/oneUAV.xml");
		//String userPath = System.getProperty("user.dir");
		//File file = new File(userPath + "/map.xml");
		ReadXMLByDom4j readXml = new ReadXMLByDom4j();
		List<MapInfo> mapInfoList = readXml.getMapInfo(file);
		for(MapInfo info : mapInfoList){
			if (info.getType()=="origin") {
				new CoordinateTransformation(info.getData().get(0));
			}else {
				PolygonFactory.createPolygon(info, true);
			}
		}
   
		int obstacleNumber = Map.getInstance().obstacles.size();
		for(int i = 0;i<obstacleNumber;i++) {
			Map.getInstance().obstacles.get(i).triDecompose();
		}
		Map.getInstance().print();
		dataExport.mapOutput();

		Map.getInstance().createGrid();
		dataExport.linesOutput(Grid.getGridLines());
		
//		DistributeGrid dg = new DistributeGrid(8);
//		dg.printGrouped();
//		dg.distribute();
//		for(int i=0;i<8;i++) {
//			dataExport.linesOutput(dg.groups.get(i),i);
//		}
//		dg.test();
		
		Map.getInstance().stations.get(0).arrangeTakeOffPoint(1);
		dataExport.takeOffPointsOutput();
		
		UAV anUAV= new UAV(Map.getInstance().stations.get(0));
		
		//CreateTrajectoryByGA ct = new CreateTrajectoryByGA();
		//anUAV.trajectory = ct.createTrajectory();
		
		anUAV.creatTrajectory();
		
		dataExport.trajectoryOutput();
		dataExport.trajectoryOutputForGeography();
		
		
	}

}
