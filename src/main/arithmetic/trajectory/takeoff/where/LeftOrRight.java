package main.arithmetic.trajectory.takeoff.where;

import main.arithmetic.trajectory.takeoff.how.FromLeftOrRight;
import main.arithmetic.trajectory.takeoff.how.TakeoffStrategy;
import main.data.SimUtils;
import main.entity.Land;
import main.entity.UAV;

public class LeftOrRight {
	public int where(Land land) {
		double left = 0;
		double right = 0;
		TakeoffStrategy fromLeft = new FromLeftOrRight(SimUtils.LEFT);
		TakeoffStrategy fromRight = new FromLeftOrRight(SimUtils.RIGHT);
		for(UAV uav:land.UAVs) {
			left += fromLeft.takeOff(uav.getPosition(), uav.getGridLines()).distanceToPoint(uav.getPosition());
			right += fromRight.takeOff(uav.getPosition(), uav.getGridLines()).distanceToPoint(uav.getPosition());
		}
		if(left<right) {
			return SimUtils.LEFT;
		}else {
			return SimUtils.RIGHT;
		}
	}
}
