package com.graduate.main;

import com.graduate.commitData.*;

public class Main {

	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub
//     AllCommitExtract allcommit=new AllCommitExtract();
//     allcommit.extractAllCommit();
     
//		SingleCommitDetail singleCommit=new SingleCommitDetail();
//		singleCommit.getCommitDetail();
		
		
		CommitDetailFromLocalPre detail = new CommitDetailFromLocalPre();
		detail.getCommitDetail();
		System.out.println("normal exit,all have finished!");
		
		
	}

}
