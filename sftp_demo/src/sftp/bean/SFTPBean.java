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
//SFTP 함수,클래스
public class SFTPBean {
	//rsa키생성
	
	
	
	//sftp채널에 사용될 변
	private JSch JSession = null;
	private Session SSHSession = null;
	
	//sftp 채널
	private ChannelSftp SftpChannel = null;

	//connect fucntion let connect to sftp server
	//in here or any protocol please remember some the other variable very important
	public boolean connect(String strHostAddress, int iPort, String strUserName, String strPassword) {
		boolean blResult = false;

		try {
			//jsch 세션 생성부분.
			this.JSession = new JSch();
			
			//set sftp server no check key when login
			java.util.Properties config = new java.util.Properties();
			config.put("StrictHostKeyChecking", "no");
			this.JSession.setConfig(config);

			//사용자의 세션, 호스트 포트 생성
			this.SSHSession = JSession.getSession(strUserName, strHostAddress, iPort);
			System.out.println("세션만들어짐");
		
			this.SSHSession.setPassword(strPassword);
			System.out.println("패스워드 설정");
			
			this.SSHSession.connect();
			System.out.println("세션연결");
			
			this.SftpChannel = (ChannelSftp) this.SSHSession.openChannel("sftp");
			System.out.println("sftp채널오픈");
			//sftp채널 연결 true / false
			this.SftpChannel.connect();
			if (this.SftpChannel != null) {
				blResult = true;
			}
			System.out.println("sftp채널연결");
		} catch (Exception exp) {
			exp.printStackTrace();
		}
		return blResult;
	}

	//sftp 파일의 리스트
	public Vector<LsEntry> listFile(String strPath) {
		Vector<LsEntry> vtFile = null;

		try {
			vtFile = this.SftpChannel.ls(strPath);
		} catch (Exception exp) {
			exp.printStackTrace();
		}
		return vtFile;
	}

	//파일 다운로드 , 업로드함수
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

	//세션 닫기
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
