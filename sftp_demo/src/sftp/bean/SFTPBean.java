package sftp.bean;

import java.util.Vector;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import java.io.*;
import java.security.*;
import java.security.spec.*;
import javax.crypto.*;
import org.apache.commons.codec.binary.Base64;
//SFTP �Լ�,Ŭ����
public class SFTPBean {
	//rsaŰ����
	
	
	
	//sftpä�ο� ���� ��
	private JSch JSession = null;
	private Session SSHSession = null;
	
	//sftp ä��
	private ChannelSftp SftpChannel = null;

	//connect fucntion let connect to sftp server
	//in here or any protocol please remember some the other variable very important
	public boolean connect(String strHostAddress, int iPort, String strUserName, String strPassword) {
		boolean blResult = false;

		try {
			//jsch ���� �����κ�.
			this.JSession = new JSch();
			
			//set sftp server no check key when login
			java.util.Properties config = new java.util.Properties();
			config.put("StrictHostKeyChecking", "no");
			this.JSession.setConfig(config);

			//������� ����, ȣ��Ʈ ��Ʈ ����
			this.SSHSession = JSession.getSession(strUserName, strHostAddress, iPort);
			System.out.println("���Ǹ������");
		
			this.SSHSession.setPassword(strPassword);
			System.out.println("�н����� ����");
			
			this.SSHSession.connect();
			System.out.println("���ǿ���");
			
			this.SftpChannel = (ChannelSftp) this.SSHSession.openChannel("sftp");
			System.out.println("sftpä�ο���");
			//sftpä�� ���� true / false
			this.SftpChannel.connect();
			if (this.SftpChannel != null) {
				blResult = true;
			}
			System.out.println("sftpä�ο���");
		} catch (Exception exp) {
			exp.printStackTrace();
		}
		return blResult;
	}

	//sftp ������ ����Ʈ
	public Vector<LsEntry> listFile(String strPath) {
		Vector<LsEntry> vtFile = null;

		try {
			vtFile = this.SftpChannel.ls(strPath);
		} catch (Exception exp) {
			exp.printStackTrace();
		}
		return vtFile;
	}

	//���� �ٿ�ε� , ���ε��Լ�
	public boolean downloadFile(String strSftpFile, String strLocalFile) {
		boolean blResult = false;

		try {
			this.SftpChannel.get(strSftpFile, strLocalFile);
			blResult = true;
		} catch (Exception exp) {
			exp.printStackTrace();
		}
		return blResult;
	}


	public boolean uploadFile(String strLocalFile, String strSftpFile) {
		boolean blResult = false;

		try {
			this.SftpChannel.put(strLocalFile, strSftpFile);
			blResult = true;
		} catch (Exception exp) {
			exp.printStackTrace();
		}
		return blResult;
	}

	//���� �ݱ�
	public void close() {
		try {
			this.SftpChannel.disconnect();
		} catch (Exception exp) {

		}

		try {
			this.SSHSession.disconnect();
		} catch (Exception exp) {

		}

		this.SftpChannel = null;
		this.SSHSession = null;
		this.JSession = null;
	}
}
