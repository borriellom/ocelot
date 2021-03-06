package com.vistatec.ocelot.config.xml;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;

/**
 * XML TM configuration object.
 */
public class TmManagement {
    private double fuzzyThreshold;
    private int maxResults;
    private List<TmConfig> tm;

    public TmManagement() {
        this.tm = new ArrayList<>();
    }

    @XmlElement
    public double getFuzzyThreshold() {
        return fuzzyThreshold;
    }

    public void setFuzzyThreshold(double fuzzyThreshold) {
        this.fuzzyThreshold = fuzzyThreshold;
    }

    @XmlElement
    public int getMaxResults() {
        return maxResults;
    }

    public void setMaxResults(int maxResults) {
        this.maxResults = maxResults;
    }

    @XmlElement
    public List<TmConfig> getTms() {
        return tm;
    }

    public void setTm(List<TmConfig> tm) {
        this.tm = tm;
    }

    public static class TmConfig {
        private String tmName;
        private boolean enabled;
        private String tmDataDir;
        private TmxFiles tmxFiles; 
        private float penalty;

        @XmlElement
        public String getTmName() {
            return tmName;
        }

        public void setTmName(String tmName) {
            this.tmName = tmName;
        }

        @XmlElement
        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        @XmlElement
        public String getTmDataDir() {
            return tmDataDir;
        }

        public void setTmDataDir(String tmDataDir) {
            this.tmDataDir = tmDataDir;
        }

        @XmlElement
        public float getPenalty() {
            return penalty;
        }

        public void setPenalty(float penalty) {
            this.penalty = penalty;
        }
        
        @XmlElement
        public TmxFiles getTmxFiles(){
        	return tmxFiles;
        }
        
        
        public void setTmxFiles(TmxFiles tmxFiles){
        	this.tmxFiles = tmxFiles;
        }
        
        public static class TmxFiles{
        	
        	private List<String> files;
        	
        	public List<String> getTmxFile(){
        		return files;
        	}
        	@XmlElement
        	public void setTmxFile(List<String> files){
        		this.files = files;
        	}
        }
        
        
//        public static class TmxFile {
//        	
//        	private String fileName;
//        	
////        	public TmxFile() {
////            }
////        	
////        	public TmxFile(String tmxFile) {
////        		this.tmxFile = tmxFile;
////            }
//
//    		public String getFileName() {
//    			return fileName;
//    		}
//
//        	@XmlElement
//    		public void setFileName(String fileName) {
//    			this.fileName = fileName;
//    		}
//        	
//        }

    }
    
   
}
