import java.util.ArrayList;

public class Path {
	
	private ArrayList<Node> sequence = new ArrayList<Node>();
	//private double length = 0;
	
	public Path() {}
	
	public Path(Node startNode) {
		
		sequence.add(startNode);
		
	}
	
	public ArrayList<Node> getSequence() {
		return sequence;
	}
	
	public void setSequence(ArrayList<Node> pathSequence) {
		this.sequence = pathSequence;
	}
	
	public double getLength() {
		
		double distance = 0;
		for (int i = 0; i < getSequence().size() - 1; i++) {
			
			distance += getSequence().get(i).getConnectionTo(
							getSequence().get(i+1)).getLength();
			
		}
		
		return distance;
		
	}
	
	public ArrayList<Connection> getConnections() {
		
		ArrayList<Connection> connections = new ArrayList<Connection>();
		
		for (int i = 0; i < getSequence().size() - 1; i++) {
			
			connections.add(getSequence().get(i).getConnectionTo(getSequence().get(i+1)));
			
		}
		
		return connections;
		
	}
	
	public void addNode(Node nextNode) {
		
		getSequence().add(nextNode);
		
		// TODO I think the below can be removed
		/*
		if (!sequence.isEmpty() && sequence.get(sequence.size() - 1) != nextNode) {
			
			Node lastNode = sequence.get(sequence.size() - 1);

			//getSequence().add(nextNode);
			
			if (lastNode.isConnectedTo(nextNode)) {

				getSequence().add(nextNode);
				//setLength(getLength() + lastNode.getConnectionTo(nextNode).getLength());

			} else {
				
				
				
			}
			
		} else {
			
			getSequence().add(nextNode); 
			
		}*/
		
	}
	
	/**
	 * Reverse the order of Nodes in the Path's sequence
	 */
	public void reverseSequence() {
		
		Path newPath = new Path();
		for (int i = getSequence().size() - 1; i >= 0; i--) {
			
			newPath.addNode(getSequence().get(i));
			
		}
		
		setSequence(newPath.getSequence());
		
		
	}
	
	@Override
	public String toString() {
		
		String str = "[";
		for (int i = 0; i < getSequence().size() - 1; i++) {
			
			str += getSequence().get(i).getNumber() + " -> ";
			
		}
		
		str += getSequence().get(getSequence().size() - 1).getNumber() + "]";
		
		return str;
		
	}
	
}
