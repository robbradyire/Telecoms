package Packets;

import Overhead.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.InetSocketAddress;
import java.net.SocketException;

/**
 * PingResponse
 * 
 * @author Tomas Barry
 * 
 */
public class PingResponse extends PacketContent {
	private Client controller;

	/**
	 * constructor
	 */
	public PingResponse(Client responder) {
		this.type = PING_RESPONSE;
		this.controller = responder;
	}

	/**
	 * PingRequest constructor
	 * 
	 * @param oin: ObjectInputStream that contains data about the packet
	 */
	protected PingResponse(ObjectInputStream oin) {
		try {
			this.type = PING_RESPONSE;
			this.controller = (Client) oin.readObject();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * send
	 * sends a PingResponse to the Server
	 * 
	 * @throws IOException
	 * @throws SocketException
	 */
	public void send() {
		try {
			DatagramPacket packet = this.toDatagramPacket();
			packet.setSocketAddress(this.getDestAddress());
			this.controller.socket.send(packet);
		}
		catch (SocketException e) {
			// no action
		}
		catch (IOException e) {
			// no action
		}
	}

	/**
	 * TODO
	 * 
	 * @return
	 */
	public InetSocketAddress getDestAddress() {
		return this.controller.dstAddress;
	}

	/**
	 * tooObjectOutputStream
	 * writes content into an ObjectOutputStream
	 * 
	 * @param out: output stream to write
	 */
	protected void toObjectOutputStream(ObjectOutputStream out) {
		try{
			this.type = PING_RESPONSE;
			out.writeObject(controller);
		} catch (Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}

}