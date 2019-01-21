package main.entity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import main.arithmetic.Dijkstra;
import main.arithmetic.SimUtils;
import main.entity.geometry.LineSegment;
import main.entity.geometry.Point;
import main.entity.geometry.Polygon;
/**
 * 地图：
 * 采用单例模式；
 * 地图由land和obstacle等组成，在它们初始化时自动加入到map
 */
public class Map {
	/**
	 * 四类实体分别存储在lands,obstacles,stations,UAVs
	 */
	public final List<Land> lands = new ArrayList<Land>();
	public final List<Obstacle> obstacles = new ArrayList<Obstacle>();
	public final List<Station> stations = new ArrayList<Station>();
	public final List<UAV> UAVs = new ArrayList<UAV>();
	public List<LineSegment> gridLines;
	public List<Point> gridPoints;
	
	private CreateTrajectory createTrajectory;
	public final static double TP = 10;
	/**
	 * 安全距离
	 */
	public static double safetyDistance = SimUtils.SAFETYDISTANCE;

	private static Map map = new Map();
	private Map() {	}
	public static Map getInstance(){
		return map;
	}

	private void setGrid() {
		gridLines= Grid.getGridLines();
		gridPoints =  Grid.getGridPoints();
		//System.out.println(gridPoints.size());
		for(Station station:stations) {
			for(TakeOffPoint tp:station.takeOffPoints) {
				gridPoints.remove(tp);
			}
		}
	}
	/**
	 * 生成网格线及其对应的网格点
	 */
	public void createGrid() {
		//Date currentTime = new Date();
	    //SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		for(Land land:lands) {
		    System.out.println(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
			System.out.println("-----------开始划分第" + Integer.toString(lands.indexOf(land)+1) +"快地-----------");
			System.out.println(land.toString());
			land.createGridLines();
			//for(LineSegment line:gridLines) {System.out.println(line);}
			land.devideGridLinesByObstacle(obstacles);
			Grid.add(land.getGridLines());
			System.out.println("----------- --END---------------");
		}
		setGrid();
	}
	
	/**
	 * 清除某个实体（land、obstacle或station）。
	 * @param polygon
	 */
	public void removePolygon(Polygon polygon) {
		if(polygon instanceof Land) {
			this.lands.remove(polygon);
		}
		if(polygon instanceof Obstacle) {
			this.obstacles.remove(polygon);
		}
		if(polygon instanceof Station) {
			this.stations.remove(polygon);
		}
	}
	
	/**
	 * 增加某个实体（land、obstacle或station）。
	 * @param polygon
	 */
	public void addPolygon(Polygon polygon) {
		if(polygon instanceof Land) {
			this.lands.add((Land) polygon);
		}
		if(polygon instanceof Obstacle) {
			this.obstacles.add((Obstacle) polygon);
		}
		if(polygon instanceof Station) {
			this.stations.add((Station) polygon);
		}
	}

	public CreateTrajectory getCreateTrajectory() {
		return createTrajectory;
	}
	public void setCreateTrajectory(CreateTrajectory createTrajectory) {
		this.createTrajectory = createTrajectory;
	}
	
	public void print() {
		System.out.println(this.toString());
	}

	public String toString() {
		String str="=======================Map=======================\t\n";
		str+= ">>>>>>共有" + lands.size() + "快Land:\t\n";
		for(Land land:lands) {
			str+=land.toString()+" \t\n";
		}
		str+= ">>>>>>共有" + obstacles.size() + "快Obstacle:\t\n";
		for(Obstacle obstacle:obstacles) {
			str+=obstacle.toString()+" \t\n";
		}

		str+= ">>>>>>共有" + stations.size() + "个Station:\t\n";
		for(Station station:stations) {
			str+=station.toString()+" \t\n";
		}
		str+="=======================END=======================\t\n";
		return str;
	}
}
