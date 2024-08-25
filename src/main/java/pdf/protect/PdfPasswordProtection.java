package pdf.protect;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.encryption.StandardProtectionPolicy;

import java.io.File;
import java.io.IOException;

public class PdfPasswordProtection {
    public static void addPasswordToPdf(File inputFile, File outputFile, String ownerPassword, String userPassword) throws IOException {
        try (PDDocument document = PDDocument.load(inputFile)) {
            AccessPermission accessPermission = new AccessPermission();
            StandardProtectionPolicy protectionPolicy = new StandardProtectionPolicy(ownerPassword, userPassword, accessPermission);
            protectionPolicy.setEncryptionKeyLength(128); // 암호화 키 길이 설정
            protectionPolicy.setPermissions(accessPermission);

            document.protect(protectionPolicy);
            document.save(outputFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
