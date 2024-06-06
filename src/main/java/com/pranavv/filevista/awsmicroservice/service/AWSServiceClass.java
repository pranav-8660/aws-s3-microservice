package com.pranavv.filevista.awsmicroservice.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.pranavv.filevista.awsmicroservice.proxy.Userproxy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Instant;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;


@Service
public class AWSServiceClass implements AWSServiceInterface{

    @Value("${awsBucketName}")
    private String bucketName;

    private final AmazonS3 s3;

    private final Userproxy uproxy;

    public AWSServiceClass(AmazonS3 s3, Userproxy uproxy) {
        this.s3 = s3;
        this.uproxy = uproxy;
    }

    private File convertMultiIntoFile(MultipartFile mfileInst) throws Exception{

        File fileInst = new File(mfileInst.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(fileInst);

        fos.write(mfileInst.getBytes());
        fos.close();

        return fileInst;
    }


    @Override
    public String insertFileOntoFolder(String folder, MultipartFile mfileInst) {

        File fileInst;
        try {
            fileInst = convertMultiIntoFile(mfileInst);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        s3.putObject(bucketName, folder+"/"+mfileInst.getOriginalFilename(), fileInst);

        return "Success";
    }

    private boolean createFolder(String folderName){

        s3.putObject(bucketName,folderName+"/","");
        return true;

    }

    @Override
    public boolean createFiveFolders(String userName) {

        if(createFolder(userName)){
            createFolder(userName+"/IMAGES");
            createFolder(userName+"/FILES");
            createFolder(userName+"/VIDEOS");
            createFolder(userName+"/AUDIOS");
            createFolder(userName+"/SCRATCH");
            return true;
        }
        return false;
    }

    public String addfilevalidatedintoFolder(int tag,String username,MultipartFile mfileInst){

        String filePath="",foldername="",filetype="";

        switch (tag){
            case 1:
                    filePath=username+"/IMAGES";
                    foldername = "IMAGES";
                    filetype="image";
                    break;
            case 2:
                    filePath=username+"/VIDEOS";
                    foldername = "VIDEOS";
                    filetype="video";
                    break;
            case 3:
                    filePath=username+"/AUDIOS";
                    foldername = "AUDIOS";
                    filetype="audio";
                    break;
            case 4:
                    filePath=username+"/FILES";
                    foldername = "FILES";
                    filetype="file-doc";
                    break;
            case 5:
                    filePath=username+"/SCRATCH";
                    foldername = "SCRATCH";
                    filetype="pdf";
                    break;
        }
        uproxy.addFileData(username,mfileInst.getOriginalFilename(),filetype,String.valueOf(mfileInst.getSize()));
        return insertFileOntoFolder(filePath,mfileInst);

    }

    public String addScratchFileintoFolder(String username,byte[] scratchFile) throws IOException {

        File mfileInst = new File("randFile.pdf");
        FileOutputStream fos = new FileOutputStream(mfileInst);
        fos.write(scratchFile);
        fos.close();
        Random rand = new Random();
        s3.putObject(bucketName,username+"/SCRATCH/"+rand.toString(),mfileInst);
        return "Success, please check the path:- "+username+"/SCRATCH"+java.sql.Timestamp.from(Instant.now()).toString()+" .For the pdf!";
    }

    public boolean validator(int tagno,MultipartFile file){
        boolean res=false;
        switch (tagno){
            case 1:
                if(file.getOriginalFilename().endsWith(".jpeg") || file.getOriginalFilename().endsWith(".jpg") || file.getOriginalFilename().endsWith(".png"))
                    res=true;
                break;
            case 2:
                if(file.getOriginalFilename().endsWith(".mp4"))
                    res=true;
                break;
            case 3:
                if(file.getOriginalFilename().endsWith(".mp3"))
                    res=true;
                break;
            case 4:
                if(file.getOriginalFilename().endsWith(".xlsx") || file.getOriginalFilename().endsWith(".pdf") || file.getOriginalFilename().endsWith(".txt")
                        || file.getOriginalFilename().endsWith(".csv") || file.getOriginalFilename().endsWith(".doc") || file.getOriginalFilename().endsWith(".docx"))
                    res=true;
                break;
        }
        return res;
    }

    private List<String> getAllFilesFromfolderforUSer(String username,String foldername){

        String appender = username+"/"+foldername.toUpperCase()+"/";

        ListObjectsV2Result viewInst = s3.listObjectsV2(bucketName,appender);

        return viewInst.getObjectSummaries().stream().map(obj->obj.getKey()).collect(Collectors.toList());
    }


    public List<String> FetchfilefromFolderforuser(String username, String folderName){

        String scratchString = "https://filevista-userdrive.s3.ap-south-1.amazonaws.com/";

        List<String> nameList = getAllFilesFromfolderforUSer(username,folderName);
        LinkedList<String> urlsList = new LinkedList<>();

        nameList.remove(nameList.get(0));

        Iterator<String> iter = nameList.iterator();

        int counter=0;

        while(iter.hasNext()) {
            urlsList.add(scratchString+iter.next());
        }

        return urlsList;
    }


    public String deleteaFilefromFolder(List<String> fileList){

        String key = "https://filevista-userdrive.s3.ap-south-1.amazonaws.com/";

        for(String fileUrl:fileList){
            String temp = fileUrl.substring(key.length(),fileUrl.length());
            s3.deleteObject(bucketName,temp);
        }

        return "Successfully deleted!";

    }



}
