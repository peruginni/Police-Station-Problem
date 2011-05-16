package policestation;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Ondřej Macoszek <ondra@macoszek.cz>
 */
public class Node
{
	public int id;
	public int x;
	public int y;
	public boolean isRisk = false;
	public Set<Node> neighbors = new HashSet<Node>();
	
	public int currentDistanceFromTarget;
}
