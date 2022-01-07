package competition.rl_ai.angeloquarta;

import competition.rl_ai.angeloquarta.enums.Distance;
import competition.rl_ai.angeloquarta.enums.PositionX;
import competition.rl_ai.angeloquarta.enums.PositionY;
import competition.rl_ai.angeloquarta.enums.Size;

public class ObstacleState {
	
	private Distance obstacleDistance;
	private PositionX obstacleHorizontalPosition;
	private PositionY obstacleVerticalPosition;
	private Size obstacleSize;
	
	public ObstacleState(Distance obstacleDistance, PositionX obstacleHorizontalPosition,
			PositionY obstacleVerticalPosition, Size obstacleSize) {
		super();
		this.obstacleDistance = obstacleDistance;
		this.obstacleHorizontalPosition = obstacleHorizontalPosition;
		this.obstacleVerticalPosition = obstacleVerticalPosition;
		this.obstacleSize = obstacleSize;
	}

	public Distance getObstacleDistance() {
		return obstacleDistance;
	}

	public PositionX getObstacleHorizontalPosition() {
		return obstacleHorizontalPosition;
	}

	public PositionY getObstacleVerticalPosition() {
		return obstacleVerticalPosition;
	}

	public Size getObstacleSize() {
		return obstacleSize;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((obstacleDistance == null) ? 0 : obstacleDistance.hashCode());
		result = prime * result + ((obstacleHorizontalPosition == null) ? 0 : obstacleHorizontalPosition.hashCode());
		result = prime * result + ((obstacleSize == null) ? 0 : obstacleSize.hashCode());
		result = prime * result + ((obstacleVerticalPosition == null) ? 0 : obstacleVerticalPosition.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ObstacleState other = (ObstacleState) obj;
		if (obstacleDistance != other.obstacleDistance)
			return false;
		if (obstacleHorizontalPosition != other.obstacleHorizontalPosition)
			return false;
		if (obstacleSize != other.obstacleSize)
			return false;
		if (obstacleVerticalPosition != other.obstacleVerticalPosition)
			return false;
		return true;
	}
	
}
