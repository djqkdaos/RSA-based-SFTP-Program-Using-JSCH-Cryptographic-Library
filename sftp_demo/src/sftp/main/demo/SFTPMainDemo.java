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
		
		File file1 = new File("C:\\Users\\nameok Kim\\Downloads\\UDP+ä��\\rsaKey\\PublicKey.txt");
		File file2 = new File("C:\\Users\\nameok Kim\\Downloads\\UDP+ä��\\rsaKey\\PrivateKey.txt");
		//Ű��������
		boolean isExists1 = file1.exists();
		boolean isExists2 = file2.exists();
		if(isExists1&&isExists2) {//���Ͽ� Ű�� ���� �Ѵٸ�
			//stfp�� �̿��ؼ�  rsaŰ���� ����
			SFTPBean sftpBean = new SFTPBean();

			boolean blResult = sftpBean.connect("127.0.0.1", 22, "user", "skadjr1030");//SSH���� ���� �޼ҵ�

			if (blResult) {
				System.out.println("Connect successed");
				
				//�ٿ�ε� ����κ�
				blResult = sftpBean.uploadFile( "C:\\Users\\user\\Desktop\\�б�\\��Ʈ��ũ����\\PublicKey.txt","/C:/Users/user/Desktop/�б�/��Ʈ��ũ����/upload");
				if(blResult) {
					System.out.println("upload successed");
				}
				else {
					System.out.println("failed");
				}
				blResult = sftpBean.downloadFile( "/C:/Users/user/Desktop/�б�/��Ʈ��ũ����/PublicKey.txt","/C:/Users/user/Desktop/�б�/��Ʈ��ũ����/download");
				if(blResult) {
					System.out.println("download successed");
				}
				else {
					System.out.println("d failed");
				}
	  			
				//���ε��� ���� üŷ.
				Vector<LsEntry> FilesList = sftpBean.listFile("/C:/Users/user/Desktop/�б�/��Ʈ��ũ����");
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
			//rsaŰ����
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
			//RSA Ű �� ���� ��ⷯ/����
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
	        //����Ű, ����Ű�� ��Ʈ���������� ���ڵ���.
	        byte[] bPublicKey1 = PubKey1.getEncoded();
	        String sPublicKey1 = Base64.encodeBase64String(bPublicKey1);

	        byte[] bPrivateKey1 = PriKey1.getEncoded();
	        String sPrivateKey1 = Base64.encodeBase64String(bPrivateKey1);

	        try {
	            BufferedWriter bw1 = new BufferedWriter(new FileWriter("C:\\Users\\user\\Desktop\\�б�\\��Ʈ��ũ����\\PublicKey.txt"));
	            bw1.write(sPublicKey1);
	            bw1.newLine();
	            bw1.close();
	            BufferedWriter bw2 = new BufferedWriter(new FileWriter("C:\\Users\\user\\Desktop\\�б�\\��Ʈ��ũ����\\PrivateKey.txt"));
	            bw2.write(sPrivateKey1);
	            bw2.newLine();
	            bw2.close();
	        } catch (IOException e) {
	        	e.printStackTrace();
	        }
		}
		
        
		
		
		//rsaŰ�� �̿��� ����� �Ϻ�ȣȭ�� �� �� �ִ�.
		String PubKey2 = null;
        String PriKey2 = null;
        
        BufferedReader brPubKey = null;
        BufferedReader brPriKey = null;
        try {
            brPubKey = new BufferedReader(new FileReader("C:\\Users\\user\\Desktop\\�б�\\��Ʈ��ũ����\\PublicKey.txt"));
            PubKey2 = brPubKey.readLine(); 	
            brPriKey = new BufferedReader(new FileReader("C:\\Users\\user\\Desktop\\�б�\\��Ʈ��ũ����\\PrivateKey.txt"));
            PriKey2 = brPriKey.readLine();	
            System.out.println("����Ű / ����Ű ����");
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
        //���ڵ��ߴ� Ű���� �ٽ� ���ڵ���.
        byte[] bPublicKey2 = Base64.decodeBase64(PubKey2.getBytes());
        PublicKey  publicKey2 = null;
        
        byte[] bPrivateKey2 = Base64.decodeBase64(PriKey2.getBytes());
        PrivateKey privateKey2 = null;
        //��������
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
			System.out.println("��ȣȭ��: " + sPlain1); // ��(����)
            // ����Ű �̿� ��ȣȭ
            cipher.init(Cipher.ENCRYPT_MODE, publicKey2);
            byte[] bCipher1 = cipher.doFinal(sPlain1.getBytes());
            String sCipherBase64 = Base64.encodeBase64String(bCipher1);
            System.out.println("��ȣȭ��: " + sCipherBase64); // ��(����)
            
            // ����Ű �̿� ��ȣȭ
            byte[] bCipher2 = Base64.decodeBase64(sCipherBase64.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, privateKey2);
            byte[] bPlain2 = cipher.doFinal(bCipher2);
            sPlain2 = new String(bPlain2);
            System.out.println("sPlain2 : " + sPlain2);	// ��(��ȣȭ�� ��ȣȭ�� ��)
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
