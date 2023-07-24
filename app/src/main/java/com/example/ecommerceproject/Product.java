package com.example.ecommerceproject;

public class Product {
 private String pid;
 private String pimage;
 private String ptittle;
 private String psubtittle;
 private String pcutprice;
 private String pprice;
 private String poff;

 private String pdate;

 public Product(String pid, String pimage, String ptittle, String psubtittle, String pcutprice, String pprice, String poff,String pdate) {
  this.pid = pid;
  this.pimage = pimage;
  this.ptittle = ptittle;
  this.psubtittle = psubtittle;
  this.pcutprice = pcutprice;
  this.pprice = pprice;
  this.poff = poff;
  this.pdate=pdate;
 }


 // Getter Methods 

 public String getPid() {
  return pid;
 }

 public String getPimage() {
  return pimage;
 }

 public String getPtittle() {
  return ptittle;
 }

 public String getPsubtittle() {
  return psubtittle;
 }

 public String getPcutprice() {
  return pcutprice;
 }

 public String getPprice() {
  return pprice;
 }

 public String getPoff() {
  return poff;
 }

 public String getPdate() {
  return pdate;
 }

 // Setter Methods

 public void setPid(String pid) {
  this.pid = pid;
 }

 public void setPimage(String pimage) {
  this.pimage = pimage;
 }

 public void setPtittle(String ptittle) {
  this.ptittle = ptittle;
 }

 public void setPsubtittle(String psubtittle) {
  this.psubtittle = psubtittle;
 }

 public void setPcutprice(String pcutprice) {
  this.pcutprice = pcutprice;
 }

 public void setPprice(String pprice) {
  this.pprice = pprice;
 }

 public void setPoff(String poff) {
  this.poff = poff;
 }

 public void setPdate(String pdate) {
  this.pdate = pdate;
 }
}