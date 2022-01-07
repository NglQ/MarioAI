package competition.rl_ai.angeloquarta;

import competition.rl_ai.angeloquarta.enums.Distance;
import competition.rl_ai.angeloquarta.enums.PositionX;
import competition.rl_ai.angeloquarta.enums.PositionY;
import competition.rl_ai.angeloquarta.enums.Type;

public class EnemyState {
	
	private Distance enemyDistance;
	private PositionX enemyHorizontalPosition;
	private PositionY enemyVerticalPosition;
	private Type enemyKind;
	
	public EnemyState(Distance enemyDistance, PositionX enemyHorizontalPosition, PositionY enemyVerticalPosition,
			Type enemyKind) {
		super();
		this.enemyDistance = enemyDistance;
		this.enemyHorizontalPosition = enemyHorizontalPosition;
		this.enemyVerticalPosition = enemyVerticalPosition;
		this.enemyKind = enemyKind;
	}
	public Distance getEnemyDistance() {
		return enemyDistance;
	}
	public PositionX getEnemyHorizontalPosition() {
		return enemyHorizontalPosition;
	}
	public PositionY getEnemyVerticalPosition() {
		return enemyVerticalPosition;
	}
	public Type getEnemyKind() {
		return enemyKind;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((enemyDistance == null) ? 0 : enemyDistance.hashCode());
		result = prime * result + ((enemyHorizontalPosition == null) ? 0 : enemyHorizontalPosition.hashCode());
		result = prime * result + ((enemyKind == null) ? 0 : enemyKind.hashCode());
		result = prime * result + ((enemyVerticalPosition == null) ? 0 : enemyVerticalPosition.hashCode());
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
		EnemyState other = (EnemyState) obj;
		if (enemyDistance != other.enemyDistance)
			return false;
		if (enemyHorizontalPosition != other.enemyHorizontalPosition)
			return false;
		if (enemyKind != other.enemyKind)
			return false;
		if (enemyVerticalPosition != other.enemyVerticalPosition)
			return false;
		return true;
	}
	
	
	
}
