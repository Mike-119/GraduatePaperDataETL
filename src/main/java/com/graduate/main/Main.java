package com.graduate.main;

import com.graduate.commitData.*;
import com.graduate.graduationTime.GetGraduationTime;

public class Main {

	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub
//     AllCommitExtract allcommit=new AllCommitExtract();
//     allcommit.extractAllCommit();
     
//		SingleCommitDetail singleCommit=new SingleCommitDetail();
//		singleCommit.getCommitDetail();
		
//		CommitDetailFromLocalSplit detail = new CommitDetailFromLocalSplit();
//		detail.getCommitDetail();
		
		WatchStarFork watchStarFork = new WatchStarFork();
		System.out.println(args[0]);
		System.out.println(args[1]);
		watchStarFork.getWatchStarFork(args[0], args[1]);
		
//		GetGraduationTime graduation = new GetGraduationTime();
//		graduation.getTime("D:\\graduationTime.txt");
		System.out.println("normal exit,all have finished!");
		
		
		
	}

}
