package sftp.main.demo;

import java.util.Vector;

import com.jcraft.jsch.ChannelSftp.LsEntry;

import sftp.bean.SFTPBean;
import java.io.*;
import java.security.*;
import java.security.spec.*;
import javax.crypto.*;
import org.apache.commons.codec.binary.Base64;
public class SFTPMainDemo {

	// main class let exec demo
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		File file1 = new File("C:\\Users\\nameok Kim\\Downloads\\UDP+채팅\\rsaKey\\PublicKey.txt");
		File file2 = new File("C:\\Users\\nameok Kim\\Downloads\\UDP+채팅\\rsaKey\\PrivateKey.txt");
		//키가져오기
		boolean isExists1 = file1.exists();
		boolean isExists2 = file2.exists();
		if(isExists1&&isExists2) {//파일에 키가 존재 한다면
			//stfp를 이용해서  rsa키파일 전송
			SFTPBean sftpBean = new SFTPBean();

			boolean blResult = sftpBean.connect("127.0.0.1", 22, "user", "skadjr1030");//SSH서버 접속 메소드

			if (blResult) {
				System.out.println("Connect successed");
				
				//다운로드 실행부분
				blResult = sftpBean.uploadFile( "C:\\Users\\user\\Desktop\\학교\\네트워크보안\\PublicKey.txt","/C:/Users/user/Desktop/학교/네트워크보안/upload");
				if(blResult) {
					System.out.println("upload successed");
				}
				else {
					System.out.println("failed");
				}
				blResult = sftpBean.downloadFile( "/C:/Users/user/Desktop/학교/네트워크보안/PublicKey.txt","/C:/Users/user/Desktop/학교/네트워크보안/download");
				if(blResult) {
					System.out.println("download successed");
				}
				else {
					System.out.println("d failed");
				}
	  			
				//업로드한 파일 체킹.
				Vector<LsEntry> FilesList = sftpBean.listFile("/C:/Users/user/Desktop/학교/네트워크보안");
				System.out.println("-----------------------------File List-----------------------------------------");
				if (FilesList != null) {
					for (LsEntry lsEntry : FilesList) {
						System.out.println(lsEntry.getFilename() + "\r\n");
					}
				}
				System.out.println("-------------------------------------------------------------------------------");
				sftpBean.close();
			} else {
				System.out.println("Connect failed.");
			}
		}else {
			//rsa키생성
			PublicKey PubKey1 = null;
			PrivateKey PriKey1 = null;
			
			SecureRandom secureRandom = new SecureRandom();
			KeyPairGenerator keyPairGenerator;
			try {
				keyPairGenerator = KeyPairGenerator.getInstance("RSA");
			keyPairGenerator.initialize(512, secureRandom);
			
			KeyPair keyPair = keyPairGenerator.genKeyPair();
			PubKey1 = keyPair.getPublic();
			PriKey1 = keyPair.getPrivate();
			//RSA 키 쌍 생성 모듈러/지수
			KeyFactory keyFactory1 = KeyFactory.getInstance("RSA");
			RSAPublicKeySpec rsaPublicKeySpec = keyFactory1.getKeySpec(PubKey1, RSAPublicKeySpec.class);
			RSAPrivateKeySpec rsaPrivateKeySpec = keyFactory1.getKeySpec(PriKey1, RSAPrivateKeySpec.class);
			System.out.println("Public  key modulus : " + rsaPublicKeySpec.getModulus());
			System.out.println("Public  key exponent: " + rsaPublicKeySpec.getPublicExponent());
			System.out.println("Private key modulus : " + rsaPrivateKeySpec.getModulus());
			System.out.println("Private key exponent: " + rsaPrivateKeySpec.getPrivateExponent());
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (InvalidKeySpecException e) {
				e.printStackTrace();
			}
	        //공개키, 개인키를 스트링형식으로 인코딩함.
	        byte[] bPublicKey1 = PubKey1.getEncoded();
	        String sPublicKey1 = Base64.encodeBase64String(bPublicKey1);

	        byte[] bPrivateKey1 = PriKey1.getEncoded();
	        String sPrivateKey1 = Base64.encodeBase64String(bPrivateKey1);

	        try {
	            BufferedWriter bw1 = new BufferedWriter(new FileWriter("C:\\Users\\user\\Desktop\\학교\\네트워크보안\\PublicKey.txt"));
	            bw1.write(sPublicKey1);
	            bw1.newLine();
	            bw1.close();
	            BufferedWriter bw2 = new BufferedWriter(new FileWriter("C:\\Users\\user\\Desktop\\학교\\네트워크보안\\PrivateKey.txt"));
	            bw2.write(sPrivateKey1);
	            bw2.newLine();
	            bw2.close();
	        } catch (IOException e) {
	        	e.printStackTrace();
	        }
		}
		
        
		
		
		//rsa키를 이용해 상대방과 암복호화를 할 수 있다.
		String PubKey2 = null;
        String PriKey2 = null;
        
        BufferedReader brPubKey = null;
        BufferedReader brPriKey = null;
        try {
            brPubKey = new BufferedReader(new FileReader("C:\\Users\\user\\Desktop\\학교\\네트워크보안\\PublicKey.txt"));
            PubKey2 = brPubKey.readLine(); 	
            brPriKey = new BufferedReader(new FileReader("C:\\Users\\user\\Desktop\\학교\\네트워크보안\\PrivateKey.txt"));
            PriKey2 = brPriKey.readLine();	
            System.out.println("공개키 / 개인키 읽음");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (brPubKey != null)
                    brPubKey.close();
                if (brPriKey != null)
                    brPriKey.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //인코딩했던 키들을 다시 디코딩함.
        byte[] bPublicKey2 = Base64.decodeBase64(PubKey2.getBytes());
        PublicKey  publicKey2 = null;
        
        byte[] bPrivateKey2 = Base64.decodeBase64(PriKey2.getBytes());
        PrivateKey privateKey2 = null;
        //오류검출
        try {
            KeyFactory keyFactory2 = KeyFactory.getInstance("RSA");
            
            X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(bPublicKey2);
            publicKey2 = keyFactory2.generatePublic(publicKeySpec);
            
            PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(bPrivateKey2);
            privateKey2 = keyFactory2.generatePrivate(privateKeySpec);
    
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }

        String sPlain1 = "Welcome to RSA";
        String sPlain2 = null;

		try {
			Cipher cipher = Cipher.getInstance("RSA");
			System.out.println("암호화전: " + sPlain1); // 평문(원본)
            // 공개키 이용 암호화
            cipher.init(Cipher.ENCRYPT_MODE, publicKey2);
            byte[] bCipher1 = cipher.doFinal(sPlain1.getBytes());
            String sCipherBase64 = Base64.encodeBase64String(bCipher1);
            System.out.println("암호화후: " + sCipherBase64); // 평문(원본)
            
            // 개인키 이용 복호화
            byte[] bCipher2 = Base64.decodeBase64(sCipherBase64.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, privateKey2);
            byte[] bPlain2 = cipher.doFinal(bCipher2);
            sPlain2 = new String(bPlain2);
            System.out.println("sPlain2 : " + sPlain2);	// 평문(암호화후 복호화된 평문)
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		
        
        
		
	}

}
