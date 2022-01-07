package competition.rl_ai.angeloquarta;

import competition.rl_ai.angeloquarta.enums.Distance;
import competition.rl_ai.angeloquarta.enums.MarioMode;
import competition.rl_ai.angeloquarta.enums.PositionX;
import competition.rl_ai.angeloquarta.enums.PositionY;
import competition.rl_ai.angeloquarta.enums.Size;
import competition.rl_ai.angeloquarta.enums.Type;

public class State {
	public static final int NEAR_LIMIT_DISTANCE=5;
	public static final int X_POSITION_LIMIT=1;
	public static final int Y_HIGH_POSITION_LIMIT=5;
	public static final int Y_LOW_POSITION_LIMIT=2;
	
	public static final int BIG_LIMIT_OBSTACLE=4;
	public static final int SMALL_LIMIT_OBSTACLE=2;
	
	private EnemyState enemyState;
	private ObstacleState obstacleState;
	
	MarioMode marioMode;
	//boolean stuck;

	public State(EnemyState enemyState, ObstacleState obstacleState, MarioMode marioMode) {
		super();
		this.enemyState = enemyState;
		this.obstacleState = obstacleState;
		this.marioMode = marioMode;
	}
	
	public EnemyState getEnemyState() {
		return enemyState;
	}



	public ObstacleState getObstacleState() {
		return obstacleState;
	}



	public MarioMode getMarioMode() {
		return marioMode;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((enemyState == null) ? 0 : enemyState.hashCode());
		result = prime * result + ((marioMode == null) ? 0 : marioMode.hashCode());
		result = prime * result + ((obstacleState == null) ? 0 : obstacleState.hashCode());
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
		State other = (State) obj;
		if (enemyState == null) {
			if (other.enemyState != null)
				return false;
		} else if (!enemyState.equals(other.enemyState))
			return false;
		if (marioMode != other.marioMode)
			return false;
		if (obstacleState == null) {
			if (other.obstacleState != null)
				return false;
		} else if (!obstacleState.equals(other.obstacleState))
			return false;
		return true;
	}
	
}
