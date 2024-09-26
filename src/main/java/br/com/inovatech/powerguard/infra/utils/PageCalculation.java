package br.com.inovatech.powerguard.infra.utils;

public class PageCalculation {

    // get the last page
    public static Integer getLastPage(Integer count){

        if(count == null || count < 0) return  null;

        if(count % 10 != 0) return (count / 10) + 1;

        return count / 10;
    }
}
