package pdf.protect;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.encryption.StandardProtectionPolicy;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

@Service
public class PdfService {

    public File protectPdf(MultipartFile file, String ownerPassword, String userPassword) throws IOException {
        // 업로드된 파일을 임시로 저장
        File tempFile = File.createTempFile("uploaded-", ".pdf");
        file.transferTo(tempFile);

        // 출력할 파일 위치 지정
        File outputFile = new File(tempFile.getParent(), "protected-" + file.getOriginalFilename());

        // 암호화 처리
        applyPasswordProtection(tempFile, outputFile, ownerPassword, userPassword);

        System.out.println("Temporary file created at: " + tempFile.getAbsolutePath());
        System.out.println("Protected file created at: " + outputFile.getAbsolutePath());

        // 임시 파일 삭제
        //tempFile.delete();

        // 암호화된 파일 반환
        return outputFile;


    }

    private void applyPasswordProtection(File inputFile, File outputFile, String ownerPassword, String userPassword) throws IOException {
        try (InputStream inputStream = new FileInputStream(inputFile);
             PDDocument document = PDDocument.load(inputStream);
             FileOutputStream outputStream = new FileOutputStream(outputFile)) {

            // PDF 문서에 보호 정책을 설정
            AccessPermission permission = new AccessPermission();
            StandardProtectionPolicy policy = new StandardProtectionPolicy(ownerPassword, userPassword, permission);
            policy.setEncryptionKeyLength(128); // 암호화 키 길이 설정 (128-bit)
            policy.setPermissions(permission);

            document.protect(policy);

            // 변경된 문서를 파일로 저장
            document.save(outputStream);
        }
    }

}
