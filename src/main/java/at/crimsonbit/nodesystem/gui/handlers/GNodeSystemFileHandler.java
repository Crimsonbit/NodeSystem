package at.crimsonbit.nodesystem.gui.handlers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import at.crimsonbit.nodesystem.gui.GNodeMaster;

public class GNodeSystemFileHandler {

	private GNodeMaster master;

	public void loadNodeSystem(File file) {
		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
			this.master = (GNodeMaster) ois.readObject();
			ois.close();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	
	
	public void saveNodeSystem(File file, GNodeMaster master) {
		this.master = master;
		try {
			ObjectOutputStream ous = new ObjectOutputStream(new FileOutputStream(file));
			ous.writeObject(this.master);
			ous.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public GNodeMaster getNodeMaster() {
		return this.master;
	}

}
