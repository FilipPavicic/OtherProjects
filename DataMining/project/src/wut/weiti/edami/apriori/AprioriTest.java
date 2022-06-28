package wut.weiti.edami.apriori;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;


//Expected Results are provided by using R and library arules
public class AprioriTest {
	List<Set<String>> items = Utils.readCSVTransoactionErrorHandle("Mushroom.csv", " ");
	Apriori apriori = new Apriori(items);

	@Test
	public void testGetFrequentItemSetsMaxLen1() {
		Apriori apriori = new Apriori(items);
		apriori.setMinlen(1);
		apriori.setMaxlen(1);
		apriori.setSupport(0.5);
		List<ItemSet> frequentItemSets = apriori.getFrequentItemSets();
		assertEquals("Length are not the same", 13, frequentItemSets.size());
		//First
		assertEquals(1, frequentItemSets.get(0).getSupport(),10e-4);
		assertEquals("VeilType=partial",frequentItemSets.get(0).itemsOutput()[0]);
		//Second
		assertEquals(0.9753816, frequentItemSets.get(1).getSupport(),10e-4);
		assertEquals("VeilColor=white",frequentItemSets.get(1).itemsOutput()[0]);
		//Last
		assertEquals(0.5179714, frequentItemSets.get(frequentItemSets.size()-1).getSupport(),10e-4);
		assertEquals("Class=edible",frequentItemSets.get(frequentItemSets.size()-1).itemsOutput()[0]);
		
		apriori.setSupport(0.6);
		frequentItemSets = apriori.getFrequentItemSets();
		//Last
		assertEquals(0.6075825, frequentItemSets.get(frequentItemSets.size()-1).getSupport(),10e-4);
		assertEquals("SurfaceBelowRing=smooth",frequentItemSets.get(frequentItemSets.size()-1).itemsOutput()[0]);
		
		
	}
	
	@Test
	public void testGetFrequentItemSetsMaxLen1Reverse() {
		Apriori apriori = new Apriori(items);
		apriori.setMinlen(1);
		apriori.setMaxlen(1);
		apriori.setSupport(0.6);
		List<ItemSet> frequentItemSets = apriori.getFrequentItemSets();
		assertEquals("Length are not the same", 8, frequentItemSets.size());
		//First
		assertEquals(1, frequentItemSets.get(0).getSupport(),10e-4);
		assertEquals("VeilType=partial",frequentItemSets.get(0).itemsOutput()[0]);
		//Second
		assertEquals(0.9753816, frequentItemSets.get(1).getSupport(),10e-4);
		assertEquals("VeilColor=white",frequentItemSets.get(1).itemsOutput()[0]);
		//Last
		assertEquals(0.6075825, frequentItemSets.get(frequentItemSets.size()-1).getSupport(),10e-4);
		assertEquals("SurfaceBelowRing=smooth",frequentItemSets.get(frequentItemSets.size()-1).itemsOutput()[0]);

		
		apriori.setSupport(0.5);
		frequentItemSets = apriori.getFrequentItemSets();
		//Last
		assertEquals(0.5179714, frequentItemSets.get(frequentItemSets.size()-1).getSupport(),10e-4);
		assertEquals("Class=edible",frequentItemSets.get(frequentItemSets.size()-1).itemsOutput()[0]);
		
		
	}
	
	@Test
	public void testGetFrequentItemSetsMaxLen2() {
		Apriori apriori = new Apriori(items);
		apriori.setMinlen(2);
		apriori.setMaxlen(2);
		apriori.setSupport(0.5);
		List<ItemSet> frequentItemSets = apriori.getFrequentItemSets();
		assertEquals("Length are not the same", 41, frequentItemSets.size());
		//First
		assertEquals(0.9753816, frequentItemSets.get(0).getSupport(),10e-4);
		var items = new String[]  {"VeilType=partial","VeilColor=white"};
		Arrays.sort(items);
		assertArrayEquals(items ,frequentItemSets.get(0).itemsOutput());
		//20th
		assertEquals(0.5839488, frequentItemSets.get(20).getSupport(),10e-4);
		items = new String[]  {"GillAttached=free","SurfaceBelowRing=smooth"};
		Arrays.sort(items);
		assertArrayEquals(items ,frequentItemSets.get(20).itemsOutput());
		
	}
	
	@Test
	public void testGetFrequentItemSetsMaxLen3() {
		Apriori apriori = new Apriori(items);
		apriori.setMinlen(1);
		apriori.setMaxlen(3);
		apriori.setSupport(0.2);
		List<ItemSet> frequentItemSets = apriori.getFrequentItemSets();
		assertEquals("Length are not the same", 1661, frequentItemSets.size());
	}
	@Test
	public void testGetFrequentItemSetsMaxLen10() {
		Apriori apriori = new Apriori(items);
		apriori.setMinlen(1);
		apriori.setMaxlen(10);
		apriori.setSupport(0.4);
		List<ItemSet> frequentItemSets = apriori.getFrequentItemSets();
		assertEquals("Length are not the same", 533, frequentItemSets.size());
	}
	
	@Test
	public void testgetRulesMaxLen2() {
		Apriori apriori = new Apriori(items);
		apriori.setMinlen(2);
		apriori.setMaxlen(2);
		apriori.setSupport(0.6);
		apriori.setConfidence(0.4);
		List<Rule> rules = apriori.getRules();
		assertEquals("Length are not the same", 36, rules.size());
		Set<Rule> rulesTest = new HashSet<>();
		rulesTest.add(new Rule(new ItemSet("\"SurfaceBelowRing=smooth\""),new ItemSet("\"VeilType=partial\""),0.6075825,1.0000000,1.0000000));
		rulesTest.add(new Rule(new ItemSet("\"SurfaceAboveRing=smooth\""),new ItemSet("\"VeilType=partial\""),0.6371246,1.0000000,1.0000000));
		rulesTest.add(new Rule(new ItemSet("\"GillSize=broad\""),new ItemSet("\"VeilType=partial\""),0.6907927,1.0000000,1.0000000));
		rulesTest.add(new Rule(new ItemSet("\"GillSpace=close\""),new ItemSet("\"VeilType=partial\""),0.8385032,1.0000000,1.0000000));
		rulesTest.add(new Rule(new ItemSet("\"RingNumber=one\""),new ItemSet("\"VeilType=partial\""),0.9217134,1.0000000,1.0000000));
		rulesTest.add(new Rule(new ItemSet("\"GillAttached=free\""),new ItemSet("\"VeilType=partial\""),0.9741507,1.0000000,1.0000000));
		rulesTest.add(new Rule(new ItemSet("\"VeilColor=white\""),new ItemSet("\"VeilType=partial\""),0.9753816,1.0000000,1.0000000));
		rulesTest.add(new Rule(new ItemSet("\"GillAttached=free\""),new ItemSet("\"VeilColor=white\""),0.9731659,0.9989891,1.0242034));
		rulesTest.add(new Rule(new ItemSet("\"VeilColor=white\""),new ItemSet("\"GillAttached=free\""),0.9731659,0.9977284,1.0242034));
		rulesTest.add(new Rule(new ItemSet("\"VeilType=partial\""),new ItemSet("\"VeilColor=white\""),0.9753816,0.9753816,1.0000000));
		rulesTest.add(new Rule(new ItemSet("\"RingNumber=one\""),new ItemSet("\"GillAttached=free\""),0.8980798,0.9743590,1.0002138));
		rulesTest.add(new Rule(new ItemSet("\"VeilType=partial\""),new ItemSet("\"GillAttached=free\""),0.9741507,0.9741507,1.0000000));
		rulesTest.add(new Rule(new ItemSet("\"RingNumber=one\""),new ItemSet("\"VeilColor=white\""),0.8970950,0.9732906,0.9978562));
		rulesTest.add(new Rule(new ItemSet("\"GillSpace=close\""),new ItemSet("\"VeilColor=white\""),0.8148695,0.9718144,0.9963428));
		rulesTest.add(new Rule(new ItemSet("\"GillSpace=close\""),new ItemSet("\"GillAttached=free\""),0.8126539,0.9691720,0.9948893));
		rulesTest.add(new Rule(new ItemSet("\"GillSize=broad\""),new ItemSet("\"VeilColor=white\""),0.6671590,0.9657876,0.9901639));
		rulesTest.add(new Rule(new ItemSet("\"SurfaceAboveRing=smooth\""),new ItemSet("\"GillAttached=free\""),0.6134909,0.9629057,0.9884567));
		rulesTest.add(new Rule(new ItemSet("\"SurfaceAboveRing=smooth\""),new ItemSet("\"VeilColor=white\""),0.6134909,0.9629057,0.9872092));
		rulesTest.add(new Rule(new ItemSet("\"GillSize=broad\""),new ItemSet("\"GillAttached=free\""),0.6649434,0.9625802,0.9881225));
		rulesTest.add(new Rule(new ItemSet("\"GillSpace=close\""),new ItemSet("\"RingNumber=one\""),0.7956672,0.9489137,1.0295105));
		rulesTest.add(new Rule(new ItemSet("\"GillAttached=free\""),new ItemSet("\"RingNumber=one\""),0.8980798,0.9219105,1.0002138));
		rulesTest.add(new Rule(new ItemSet("\"VeilType=partial\""),new ItemSet("\"RingNumber=one\""),0.9217134,0.9217134,1.0000000));
		rulesTest.add(new Rule(new ItemSet("\"VeilColor=white\""),new ItemSet("\"RingNumber=one\""),0.8970950,0.9197375,0.9978562));
		rulesTest.add(new Rule(new ItemSet("\"GillSize=broad\""),new ItemSet("\"RingNumber=one\""),0.6125062,0.8866714,0.9619817));
		rulesTest.add(new Rule(new ItemSet("\"RingNumber=one\""),new ItemSet("\"GillSpace=close\""),0.7956672,0.8632479,1.0295105));
		rulesTest.add(new Rule(new ItemSet("\"VeilType=partial\""),new ItemSet("\"GillSpace=close\""),0.8385032,0.8385032,1.0000000));
		rulesTest.add(new Rule(new ItemSet("\"VeilColor=white\""),new ItemSet("\"GillSpace=close\""),0.8148695,0.8354366,0.9963428));
		rulesTest.add(new Rule(new ItemSet("\"GillAttached=free\""),new ItemSet("\"GillSpace=close\""),0.8126539,0.8342178,0.9948893));
		rulesTest.add(new Rule(new ItemSet("\"VeilType=partial\""),new ItemSet("\"GillSize=broad\""),0.6907927,0.6907927,1.0000000));
		rulesTest.add(new Rule(new ItemSet("\"VeilColor=white\""),new ItemSet("\"GillSize=broad\""),0.6671590,0.6839980,0.9901639));
		rulesTest.add(new Rule(new ItemSet("\"GillAttached=free\""),new ItemSet("\"GillSize=broad\""),0.6649434,0.6825878,0.9881225));
		rulesTest.add(new Rule(new ItemSet("\"RingNumber=one\""),new ItemSet("\"GillSize=broad\""),0.6125062,0.6645299,0.9619817));
		rulesTest.add(new Rule(new ItemSet("\"VeilType=partial\""),new ItemSet("\"SurfaceAboveRing=smooth\""),0.6371246,0.6371246,1.0000000));
		rulesTest.add(new Rule(new ItemSet("\"GillAttached=free\""),new ItemSet("\"SurfaceAboveRing=smooth\""),0.6134909,0.6297700,0.9884567));
		rulesTest.add(new Rule(new ItemSet("\"VeilColor=white\""),new ItemSet("\"SurfaceAboveRing=smooth\""),0.6134909,0.6289753,0.9872092));
		rulesTest.add(new Rule(new ItemSet("\"VeilType=partial\""),new ItemSet("\"SurfaceBelowRing=smooth\""),0.6075825,0.6075825,1.0000000));
	
		for(Rule rule: rules) {
			assertEquals(rule.toString(),true, rulesTest.contains(rule));
		}
			
	}
	@Test
	public void testgetRulesChangeSupportUp() {
		Apriori apriori = new Apriori(items);
		apriori.setMinlen(2);
		apriori.setMaxlen(2);
		apriori.setSupport(0.6);
		apriori.setConfidence(0.4);
		List<Rule> rules = apriori.getRules();
		apriori.setSupport(0.7);
		rules = apriori.getRules();
		assertEquals("Length are not the same",20 , rules.size());
		Set<Rule> rulesTest = new HashSet<>();
		rulesTest.add(new Rule(new ItemSet("\"GillSpace=close\""),new ItemSet("\"VeilType=partial\""),0.8385032,1.0000000,1.0000000));
		rulesTest.add(new Rule(new ItemSet("\"RingNumber=one\""),new ItemSet("\"VeilType=partial\""),0.9217134,1.0000000,1.0000000));
		rulesTest.add(new Rule(new ItemSet("\"GillAttached=free\""),new ItemSet("\"VeilType=partial\""),0.9741507,1.0000000,1.0000000));
		rulesTest.add(new Rule(new ItemSet("\"VeilColor=white\""),new ItemSet("\"VeilType=partial\""),0.9753816,1.0000000,1.0000000));
		rulesTest.add(new Rule(new ItemSet("\"GillAttached=free\""),new ItemSet("\"VeilColor=white\""),0.9731659,0.9989891,1.0242034));
		rulesTest.add(new Rule(new ItemSet("\"VeilColor=white\""),new ItemSet("\"GillAttached=free\""),0.9731659,0.9977284,1.0242034));
		rulesTest.add(new Rule(new ItemSet("\"VeilType=partial\""),new ItemSet("\"VeilColor=white\""),0.9753816,0.9753816,1.0000000));
		rulesTest.add(new Rule(new ItemSet("\"RingNumber=one\""),new ItemSet("\"GillAttached=free\""),0.8980798,0.9743590,1.0002138));
		rulesTest.add(new Rule(new ItemSet("\"VeilType=partial\""),new ItemSet("\"GillAttached=free\""),0.9741507,0.9741507,1.0000000));
		rulesTest.add(new Rule(new ItemSet("\"RingNumber=one\""),new ItemSet("\"VeilColor=white\""),0.8970950,0.9732906,0.9978562));
		rulesTest.add(new Rule(new ItemSet("\"GillSpace=close\""),new ItemSet("\"VeilColor=white\""),0.8148695,0.9718144,0.9963428));
		rulesTest.add(new Rule(new ItemSet("\"GillSpace=close\""),new ItemSet("\"GillAttached=free\""),0.8126539,0.9691720,0.9948893));
		rulesTest.add(new Rule(new ItemSet("\"GillSpace=close\""),new ItemSet("\"RingNumber=one\""),0.7956672,0.9489137,1.0295105));
		rulesTest.add(new Rule(new ItemSet("\"GillAttached=free\""),new ItemSet("\"RingNumber=one\""),0.8980798,0.9219105,1.0002138));
		rulesTest.add(new Rule(new ItemSet("\"VeilType=partial\""),new ItemSet("\"RingNumber=one\""),0.9217134,0.9217134,1.0000000));
		rulesTest.add(new Rule(new ItemSet("\"VeilColor=white\""),new ItemSet("\"RingNumber=one\""),0.8970950,0.9197375,0.9978562));
		rulesTest.add(new Rule(new ItemSet("\"RingNumber=one\""),new ItemSet("\"GillSpace=close\""),0.7956672,0.8632479,1.0295105));
		rulesTest.add(new Rule(new ItemSet("\"VeilType=partial\""),new ItemSet("\"GillSpace=close\""),0.8385032,0.8385032,1.0000000));
		rulesTest.add(new Rule(new ItemSet("\"VeilColor=white\""),new ItemSet("\"GillSpace=close\""),0.8148695,0.8354366,0.9963428));
		rulesTest.add(new Rule(new ItemSet("\"GillAttached=free\""),new ItemSet("\"GillSpace=close\""),0.8126539,0.8342178,0.9948893));
		
		for(Rule rule: rules) {
			assertEquals(rule.toString(),true, rulesTest.contains(rule));
		}
			
	}
	@Test
	public void testgetRulesChangeSupportDown() {
		Apriori apriori = new Apriori(items);
		apriori.setMinlen(2);
		apriori.setMaxlen(2);
		apriori.setSupport(0.6);
		apriori.setConfidence(0.4);
		List<Rule> rules = apriori.getRules();
		apriori.setSupport(0.5);
		rules = apriori.getRules();
		assertEquals("Length are not the same",82   , rules.size());
		Set<Rule> rulesTest = new HashSet<>();
		rulesTest.add(new Rule(new ItemSet("\"Class=edible\""),new ItemSet("\"VeilType=partial\""),0.5179714,1.0000000,1.0000000));
		rulesTest.add(new Rule(new ItemSet("\"ColorBelowRing=white\""),new ItemSet("\"GillAttached=free\""),0.5396356,1.0000000,1.0265353));
		rulesTest.add(new Rule(new ItemSet("\"ColorBelowRing=white\""),new ItemSet("\"VeilColor=white\""),0.5396356,1.0000000,1.0252398));
		rulesTest.add(new Rule(new ItemSet("\"ColorBelowRing=white\""),new ItemSet("\"VeilType=partial\""),0.5396356,1.0000000,1.0000000));
		rulesTest.add(new Rule(new ItemSet("\"ColorAboveRing=white\""),new ItemSet("\"GillAttached=free\""),0.5494830,1.0000000,1.0265353));
		rulesTest.add(new Rule(new ItemSet("\"ColorAboveRing=white\""),new ItemSet("\"VeilColor=white\""),0.5494830,1.0000000,1.0252398));
		rulesTest.add(new Rule(new ItemSet("\"ColorAboveRing=white\""),new ItemSet("\"VeilType=partial\""),0.5494830,1.0000000,1.0000000));
		rulesTest.add(new Rule(new ItemSet("\"StalkShape=tapering\""),new ItemSet("\"RingNumber=one\""),0.5672083,1.0000000,1.0849359));
		rulesTest.add(new Rule(new ItemSet("\"StalkShape=tapering\""),new ItemSet("\"GillAttached=free\""),0.5672083,1.0000000,1.0265353));
		rulesTest.add(new Rule(new ItemSet("\"StalkShape=tapering\""),new ItemSet("\"VeilColor=white\""),0.5672083,1.0000000,1.0252398));
		rulesTest.add(new Rule(new ItemSet("\"StalkShape=tapering\""),new ItemSet("\"VeilType=partial\""),0.5672083,1.0000000,1.0000000));
		rulesTest.add(new Rule(new ItemSet("\"Bruises=no\""),new ItemSet("\"VeilType=partial\""),0.5844412,1.0000000,1.0000000));
		rulesTest.add(new Rule(new ItemSet("\"SurfaceBelowRing=smooth\""),new ItemSet("\"VeilType=partial\""),0.6075825,1.0000000,1.0000000));
		rulesTest.add(new Rule(new ItemSet("\"SurfaceAboveRing=smooth\""),new ItemSet("\"VeilType=partial\""),0.6371246,1.0000000,1.0000000));
		rulesTest.add(new Rule(new ItemSet("\"GillSize=broad\""),new ItemSet("\"VeilType=partial\""),0.6907927,1.0000000,1.0000000));
		rulesTest.add(new Rule(new ItemSet("\"GillSpace=close\""),new ItemSet("\"VeilType=partial\""),0.8385032,1.0000000,1.0000000));
		rulesTest.add(new Rule(new ItemSet("\"RingNumber=one\""),new ItemSet("\"VeilType=partial\""),0.9217134,1.0000000,1.0000000));
		rulesTest.add(new Rule(new ItemSet("\"GillAttached=free\""),new ItemSet("\"VeilType=partial\""),0.9741507,1.0000000,1.0000000));
		rulesTest.add(new Rule(new ItemSet("\"VeilColor=white\""),new ItemSet("\"VeilType=partial\""),0.9753816,1.0000000,1.0000000));
		rulesTest.add(new Rule(new ItemSet("\"GillAttached=free\""),new ItemSet("\"VeilColor=white\""),0.9731659,0.9989891,1.0242034));
		rulesTest.add(new Rule(new ItemSet("\"VeilColor=white\""),new ItemSet("\"GillAttached=free\""),0.9731659,0.9977284,1.0242034));
		rulesTest.add(new Rule(new ItemSet("\"VeilType=partial\""),new ItemSet("\"VeilColor=white\""),0.9753816,0.9753816,1.0000000));
		rulesTest.add(new Rule(new ItemSet("\"RingNumber=one\""),new ItemSet("\"GillAttached=free\""),0.8980798,0.9743590,1.0002138));
		rulesTest.add(new Rule(new ItemSet("\"VeilType=partial\""),new ItemSet("\"GillAttached=free\""),0.9741507,0.9741507,1.0000000));
		rulesTest.add(new Rule(new ItemSet("\"RingNumber=one\""),new ItemSet("\"VeilColor=white\""),0.8970950,0.9732906,0.9978562));
		rulesTest.add(new Rule(new ItemSet("\"GillSpace=close\""),new ItemSet("\"VeilColor=white\""),0.8148695,0.9718144,0.9963428));
		rulesTest.add(new Rule(new ItemSet("\"GillSpace=close\""),new ItemSet("\"GillAttached=free\""),0.8126539,0.9691720,0.9948893));
		rulesTest.add(new Rule(new ItemSet("\"GillSize=broad\""),new ItemSet("\"VeilColor=white\""),0.6671590,0.9657876,0.9901639));
		rulesTest.add(new Rule(new ItemSet("\"SurfaceAboveRing=smooth\""),new ItemSet("\"GillAttached=free\""),0.6134909,0.9629057,0.9884567));
		rulesTest.add(new Rule(new ItemSet("\"SurfaceAboveRing=smooth\""),new ItemSet("\"VeilColor=white\""),0.6134909,0.9629057,0.9872092));
		rulesTest.add(new Rule(new ItemSet("\"GillSize=broad\""),new ItemSet("\"GillAttached=free\""),0.6649434,0.9625802,0.9881225));
		rulesTest.add(new Rule(new ItemSet("\"SurfaceBelowRing=smooth\""),new ItemSet("\"GillAttached=free\""),0.5839488,0.9611021,0.9866052));
		rulesTest.add(new Rule(new ItemSet("\"SurfaceBelowRing=smooth\""),new ItemSet("\"VeilColor=white\""),0.5839488,0.9611021,0.9853601));
		rulesTest.add(new Rule(new ItemSet("\"Bruises=no\""),new ItemSet("\"VeilColor=white\""),0.5598227,0.9578770,0.9820536));
		rulesTest.add(new Rule(new ItemSet("\"Bruises=no\""),new ItemSet("\"GillAttached=free\""),0.5585918,0.9557709,0.9811325));
		rulesTest.add(new Rule(new ItemSet("\"GillSpace=close\""),new ItemSet("\"RingNumber=one\""),0.7956672,0.9489137,1.0295105));
		rulesTest.add(new Rule(new ItemSet("\"Bruises=no\""),new ItemSet("\"RingNumber=one\""),0.5425899,0.9283909,1.0072446));
		rulesTest.add(new Rule(new ItemSet("\"GillAttached=free\""),new ItemSet("\"RingNumber=one\""),0.8980798,0.9219105,1.0002138));
		rulesTest.add(new Rule(new ItemSet("\"VeilType=partial\""),new ItemSet("\"RingNumber=one\""),0.9217134,0.9217134,1.0000000));
		rulesTest.add(new Rule(new ItemSet("\"VeilColor=white\""),new ItemSet("\"RingNumber=one\""),0.8970950,0.9197375,0.9978562));
		rulesTest.add(new Rule(new ItemSet("\"SurfaceAboveRing=smooth\""),new ItemSet("\"RingNumber=one\""),0.5829641,0.9149923,0.9927080));
		rulesTest.add(new Rule(new ItemSet("\"SurfaceBelowRing=smooth\""),new ItemSet("\"RingNumber=one\""),0.5534220,0.9108590,0.9882236));
		rulesTest.add(new Rule(new ItemSet("\"GillSize=broad\""),new ItemSet("\"RingNumber=one\""),0.6125062,0.8866714,0.9619817));
		rulesTest.add(new Rule(new ItemSet("\"RingNumber=one\""),new ItemSet("\"GillSpace=close\""),0.7956672,0.8632479,1.0295105));
		rulesTest.add(new Rule(new ItemSet("\"SurfaceAboveRing=smooth\""),new ItemSet("\"GillSpace=close\""),0.5445593,0.8547141,1.0193331));
		rulesTest.add(new Rule(new ItemSet("\"SurfaceBelowRing=smooth\""),new ItemSet("\"GillSpace=close\""),0.5150172,0.8476499,1.0109084));
		rulesTest.add(new Rule(new ItemSet("\"SurfaceBelowRing=smooth\""),new ItemSet("\"SurfaceAboveRing=smooth\""),0.5115707,0.8419773,1.3215270));
		rulesTest.add(new Rule(new ItemSet("\"VeilType=partial\""),new ItemSet("\"GillSpace=close\""),0.8385032,0.8385032,1.0000000));
		rulesTest.add(new Rule(new ItemSet("\"VeilColor=white\""),new ItemSet("\"GillSpace=close\""),0.8148695,0.8354366,0.9963428));
		rulesTest.add(new Rule(new ItemSet("\"GillAttached=free\""),new ItemSet("\"GillSpace=close\""),0.8126539,0.8342178,0.9948893));
		rulesTest.add(new Rule(new ItemSet("\"GillSize=broad\""),new ItemSet("\"GillSpace=close\""),0.5608075,0.8118318,0.9681916));
		rulesTest.add(new Rule(new ItemSet("\"SurfaceAboveRing=smooth\""),new ItemSet("\"SurfaceBelowRing=smooth\""),0.5115707,0.8029366,1.3215270));
		rulesTest.add(new Rule(new ItemSet("\"VeilType=partial\""),new ItemSet("\"GillSize=broad\""),0.6907927,0.6907927,1.0000000));
		rulesTest.add(new Rule(new ItemSet("\"VeilColor=white\""),new ItemSet("\"GillSize=broad\""),0.6671590,0.6839980,0.9901639));
		rulesTest.add(new Rule(new ItemSet("\"GillAttached=free\""),new ItemSet("\"GillSize=broad\""),0.6649434,0.6825878,0.9881225));
		rulesTest.add(new Rule(new ItemSet("\"GillSpace=close\""),new ItemSet("\"GillSize=broad\""),0.5608075,0.6688197,0.9681916));
		rulesTest.add(new Rule(new ItemSet("\"RingNumber=one\""),new ItemSet("\"GillSize=broad\""),0.6125062,0.6645299,0.9619817));
		rulesTest.add(new Rule(new ItemSet("\"GillSpace=close\""),new ItemSet("\"SurfaceAboveRing=smooth\""),0.5445593,0.6494422,1.0193331));
		rulesTest.add(new Rule(new ItemSet("\"VeilType=partial\""),new ItemSet("\"SurfaceAboveRing=smooth\""),0.6371246,0.6371246,1.0000000));
		rulesTest.add(new Rule(new ItemSet("\"RingNumber=one\""),new ItemSet("\"SurfaceAboveRing=smooth\""),0.5829641,0.6324786,0.9927080));
		rulesTest.add(new Rule(new ItemSet("\"GillAttached=free\""),new ItemSet("\"SurfaceAboveRing=smooth\""),0.6134909,0.6297700,0.9884567));
		rulesTest.add(new Rule(new ItemSet("\"VeilColor=white\""),new ItemSet("\"SurfaceAboveRing=smooth\""),0.6134909,0.6289753,0.9872092));
		rulesTest.add(new Rule(new ItemSet("\"RingNumber=one\""),new ItemSet("\"StalkShape=tapering\""),0.5672083,0.6153846,1.0849359));
		rulesTest.add(new Rule(new ItemSet("\"GillSpace=close\""),new ItemSet("\"SurfaceBelowRing=smooth\""),0.5150172,0.6142102,1.0109084));
		rulesTest.add(new Rule(new ItemSet("\"VeilType=partial\""),new ItemSet("\"SurfaceBelowRing=smooth\""),0.6075825,0.6075825,1.0000000));
		rulesTest.add(new Rule(new ItemSet("\"RingNumber=one\""),new ItemSet("\"SurfaceBelowRing=smooth\""),0.5534220,0.6004274,0.9882236));
		rulesTest.add(new Rule(new ItemSet("\"GillAttached=free\""),new ItemSet("\"SurfaceBelowRing=smooth\""),0.5839488,0.5994440,0.9866052));
		rulesTest.add(new Rule(new ItemSet("\"VeilColor=white\""),new ItemSet("\"SurfaceBelowRing=smooth\""),0.5839488,0.5986875,0.9853601));
		rulesTest.add(new Rule(new ItemSet("\"RingNumber=one\""),new ItemSet("\"Bruises=no\""),0.5425899,0.5886752,1.0072446));
		rulesTest.add(new Rule(new ItemSet("\"VeilType=partial\""),new ItemSet("\"Bruises=no\""),0.5844412,0.5844412,1.0000000));
		rulesTest.add(new Rule(new ItemSet("\"GillAttached=free\""),new ItemSet("\"StalkShape=tapering\""),0.5672083,0.5822593,1.0265353));
		rulesTest.add(new Rule(new ItemSet("\"VeilColor=white\""),new ItemSet("\"StalkShape=tapering\""),0.5672083,0.5815245,1.0252398));
		rulesTest.add(new Rule(new ItemSet("\"VeilColor=white\""),new ItemSet("\"Bruises=no\""),0.5598227,0.5739525,0.9820536));
		rulesTest.add(new Rule(new ItemSet("\"GillAttached=free\""),new ItemSet("\"Bruises=no\""),0.5585918,0.5734142,0.9811325));
		rulesTest.add(new Rule(new ItemSet("\"VeilType=partial\""),new ItemSet("\"StalkShape=tapering\""),0.5672083,0.5672083,1.0000000));
		rulesTest.add(new Rule(new ItemSet("\"GillAttached=free\""),new ItemSet("\"ColorAboveRing=white\""),0.5494830,0.5640637,1.0265353));
		rulesTest.add(new Rule(new ItemSet("\"VeilColor=white\""),new ItemSet("\"ColorAboveRing=white\""),0.5494830,0.5633518,1.0252398));
		rulesTest.add(new Rule(new ItemSet("\"GillAttached=free\""),new ItemSet("\"ColorBelowRing=white\""),0.5396356,0.5539550,1.0265353));
		rulesTest.add(new Rule(new ItemSet("\"VeilColor=white\""),new ItemSet("\"ColorBelowRing=white\""),0.5396356,0.5532559,1.0252398));
		rulesTest.add(new Rule(new ItemSet("\"VeilType=partial\""),new ItemSet("\"ColorAboveRing=white\""),0.5494830,0.5494830,1.0000000));
		rulesTest.add(new Rule(new ItemSet("\"VeilType=partial\""),new ItemSet("\"ColorBelowRing=white\""),0.5396356,0.5396356,1.0000000));
		rulesTest.add(new Rule(new ItemSet("\"VeilType=partial\""),new ItemSet("\"Class=edible\""),0.5179714,0.5179714,1.0000000));
		
		for(Rule rule: rules) {
			assertEquals(rule.toString(),true, rulesTest.contains(rule));
		}
			
	}
	
	@Test
	public void testgetRulesChangeConfidencetUp() {
		Apriori apriori = new Apriori(items);
		apriori.setMinlen(2);
		apriori.setMaxlen(2);
		apriori.setSupport(0.6);
		apriori.setConfidence(0.4);
		List<Rule> rules = apriori.getRules();
		apriori.setConfidence(0.7);
		rules = apriori.getRules();
		assertEquals("Length are not the same",28   , rules.size());
		Set<Rule> rulesTest = new HashSet<>();
		rulesTest.add(new Rule(new ItemSet("\"SurfaceBelowRing=smooth\""),new ItemSet("\"VeilType=partial\""),0.6075825,1.0000000,1.0000000));
		rulesTest.add(new Rule(new ItemSet("\"SurfaceAboveRing=smooth\""),new ItemSet("\"VeilType=partial\""),0.6371246,1.0000000,1.0000000));
		rulesTest.add(new Rule(new ItemSet("\"GillSize=broad\""),new ItemSet("\"VeilType=partial\""),0.6907927,1.0000000,1.0000000));
		rulesTest.add(new Rule(new ItemSet("\"GillSpace=close\""),new ItemSet("\"VeilType=partial\""),0.8385032,1.0000000,1.0000000));        
		rulesTest.add(new Rule(new ItemSet("\"RingNumber=one\""),new ItemSet("\"VeilType=partial\""),0.9217134,1.0000000,1.0000000));
		rulesTest.add(new Rule(new ItemSet("\"GillAttached=free\""),new ItemSet("\"VeilType=partial\""),0.9741507,1.0000000,1.0000000));      
		rulesTest.add(new Rule(new ItemSet("\"VeilColor=white\""),new ItemSet("\"VeilType=partial\""),0.9753816,1.0000000,1.0000000));        
		rulesTest.add(new Rule(new ItemSet("\"GillAttached=free\""),new ItemSet("\"VeilColor=white\""),0.9731659,0.9989891,1.0242034));       
		rulesTest.add(new Rule(new ItemSet("\"VeilColor=white\""),new ItemSet("\"GillAttached=free\""),0.9731659,0.9977284,1.0242034));       
		rulesTest.add(new Rule(new ItemSet("\"VeilType=partial\""),new ItemSet("\"VeilColor=white\""),0.9753816,0.9753816,1.0000000));        
		rulesTest.add(new Rule(new ItemSet("\"RingNumber=one\""),new ItemSet("\"GillAttached=free\""),0.8980798,0.9743590,1.0002138));        
		rulesTest.add(new Rule(new ItemSet("\"VeilType=partial\""),new ItemSet("\"GillAttached=free\""),0.9741507,0.9741507,1.0000000));      
		rulesTest.add(new Rule(new ItemSet("\"RingNumber=one\""),new ItemSet("\"VeilColor=white\""),0.8970950,0.9732906,0.9978562));
		rulesTest.add(new Rule(new ItemSet("\"GillSpace=close\""),new ItemSet("\"VeilColor=white\""),0.8148695,0.9718144,0.9963428));
		rulesTest.add(new Rule(new ItemSet("\"GillSpace=close\""),new ItemSet("\"GillAttached=free\""),0.8126539,0.9691720,0.9948893));
		rulesTest.add(new Rule(new ItemSet("\"GillSize=broad\""),new ItemSet("\"VeilColor=white\""),0.6671590,0.9657876,0.9901639));
		rulesTest.add(new Rule(new ItemSet("\"SurfaceAboveRing=smooth\""),new ItemSet("\"GillAttached=free\""),0.6134909,0.9629057,0.9884567));
		rulesTest.add(new Rule(new ItemSet("\"SurfaceAboveRing=smooth\""),new ItemSet("\"VeilColor=white\""),0.6134909,0.9629057,0.9872092));
		rulesTest.add(new Rule(new ItemSet("\"GillSize=broad\""),new ItemSet("\"GillAttached=free\""),0.6649434,0.9625802,0.9881225));
		rulesTest.add(new Rule(new ItemSet("\"GillSpace=close\""),new ItemSet("\"RingNumber=one\""),0.7956672,0.9489137,1.0295105));
		rulesTest.add(new Rule(new ItemSet("\"GillAttached=free\""),new ItemSet("\"RingNumber=one\""),0.8980798,0.9219105,1.0002138));
		rulesTest.add(new Rule(new ItemSet("\"VeilType=partial\""),new ItemSet("\"RingNumber=one\""),0.9217134,0.9217134,1.0000000));
		rulesTest.add(new Rule(new ItemSet("\"VeilColor=white\""),new ItemSet("\"RingNumber=one\""),0.8970950,0.9197375,0.9978562));
		rulesTest.add(new Rule(new ItemSet("\"GillSize=broad\""),new ItemSet("\"RingNumber=one\""),0.6125062,0.8866714,0.9619817));
		rulesTest.add(new Rule(new ItemSet("\"RingNumber=one\""),new ItemSet("\"GillSpace=close\""),0.7956672,0.8632479,1.0295105));
		rulesTest.add(new Rule(new ItemSet("\"VeilType=partial\""),new ItemSet("\"GillSpace=close\""),0.8385032,0.8385032,1.0000000));
		rulesTest.add(new Rule(new ItemSet("\"VeilColor=white\""),new ItemSet("\"GillSpace=close\""),0.8148695,0.8354366,0.9963428));
		rulesTest.add(new Rule(new ItemSet("\"GillAttached=free\""),new ItemSet("\"GillSpace=close\""),0.8126539,0.8342178,0.9948893));
		
		for(Rule rule: rules) {
			assertEquals(rule.toString(),true, rulesTest.contains(rule));
		}
			
	}
	
	@Test
	public void testgetRulesChangeConfidencetDown() {
		Apriori apriori = new Apriori(items);
		apriori.setMinlen(2);
		apriori.setMaxlen(2);
		apriori.setSupport(0.6);
		apriori.setConfidence(0.9);
		List<Rule> rules = apriori.getRules();
		apriori.setConfidence(0.7);
		rules = apriori.getRules();
		assertEquals("Length are not the same",28   , rules.size());
		Set<Rule> rulesTest = new HashSet<>();
		rulesTest.add(new Rule(new ItemSet("\"SurfaceBelowRing=smooth\""),new ItemSet("\"VeilType=partial\""),0.6075825,1.0000000,1.0000000));
		rulesTest.add(new Rule(new ItemSet("\"SurfaceAboveRing=smooth\""),new ItemSet("\"VeilType=partial\""),0.6371246,1.0000000,1.0000000));
		rulesTest.add(new Rule(new ItemSet("\"GillSize=broad\""),new ItemSet("\"VeilType=partial\""),0.6907927,1.0000000,1.0000000));
		rulesTest.add(new Rule(new ItemSet("\"GillSpace=close\""),new ItemSet("\"VeilType=partial\""),0.8385032,1.0000000,1.0000000));        
		rulesTest.add(new Rule(new ItemSet("\"RingNumber=one\""),new ItemSet("\"VeilType=partial\""),0.9217134,1.0000000,1.0000000));
		rulesTest.add(new Rule(new ItemSet("\"GillAttached=free\""),new ItemSet("\"VeilType=partial\""),0.9741507,1.0000000,1.0000000));      
		rulesTest.add(new Rule(new ItemSet("\"VeilColor=white\""),new ItemSet("\"VeilType=partial\""),0.9753816,1.0000000,1.0000000));        
		rulesTest.add(new Rule(new ItemSet("\"GillAttached=free\""),new ItemSet("\"VeilColor=white\""),0.9731659,0.9989891,1.0242034));       
		rulesTest.add(new Rule(new ItemSet("\"VeilColor=white\""),new ItemSet("\"GillAttached=free\""),0.9731659,0.9977284,1.0242034));       
		rulesTest.add(new Rule(new ItemSet("\"VeilType=partial\""),new ItemSet("\"VeilColor=white\""),0.9753816,0.9753816,1.0000000));        
		rulesTest.add(new Rule(new ItemSet("\"RingNumber=one\""),new ItemSet("\"GillAttached=free\""),0.8980798,0.9743590,1.0002138));        
		rulesTest.add(new Rule(new ItemSet("\"VeilType=partial\""),new ItemSet("\"GillAttached=free\""),0.9741507,0.9741507,1.0000000));      
		rulesTest.add(new Rule(new ItemSet("\"RingNumber=one\""),new ItemSet("\"VeilColor=white\""),0.8970950,0.9732906,0.9978562));
		rulesTest.add(new Rule(new ItemSet("\"GillSpace=close\""),new ItemSet("\"VeilColor=white\""),0.8148695,0.9718144,0.9963428));
		rulesTest.add(new Rule(new ItemSet("\"GillSpace=close\""),new ItemSet("\"GillAttached=free\""),0.8126539,0.9691720,0.9948893));
		rulesTest.add(new Rule(new ItemSet("\"GillSize=broad\""),new ItemSet("\"VeilColor=white\""),0.6671590,0.9657876,0.9901639));
		rulesTest.add(new Rule(new ItemSet("\"SurfaceAboveRing=smooth\""),new ItemSet("\"GillAttached=free\""),0.6134909,0.9629057,0.9884567));
		rulesTest.add(new Rule(new ItemSet("\"SurfaceAboveRing=smooth\""),new ItemSet("\"VeilColor=white\""),0.6134909,0.9629057,0.9872092));
		rulesTest.add(new Rule(new ItemSet("\"GillSize=broad\""),new ItemSet("\"GillAttached=free\""),0.6649434,0.9625802,0.9881225));
		rulesTest.add(new Rule(new ItemSet("\"GillSpace=close\""),new ItemSet("\"RingNumber=one\""),0.7956672,0.9489137,1.0295105));
		rulesTest.add(new Rule(new ItemSet("\"GillAttached=free\""),new ItemSet("\"RingNumber=one\""),0.8980798,0.9219105,1.0002138));
		rulesTest.add(new Rule(new ItemSet("\"VeilType=partial\""),new ItemSet("\"RingNumber=one\""),0.9217134,0.9217134,1.0000000));
		rulesTest.add(new Rule(new ItemSet("\"VeilColor=white\""),new ItemSet("\"RingNumber=one\""),0.8970950,0.9197375,0.9978562));
		rulesTest.add(new Rule(new ItemSet("\"GillSize=broad\""),new ItemSet("\"RingNumber=one\""),0.6125062,0.8866714,0.9619817));
		rulesTest.add(new Rule(new ItemSet("\"RingNumber=one\""),new ItemSet("\"GillSpace=close\""),0.7956672,0.8632479,1.0295105));
		rulesTest.add(new Rule(new ItemSet("\"VeilType=partial\""),new ItemSet("\"GillSpace=close\""),0.8385032,0.8385032,1.0000000));
		rulesTest.add(new Rule(new ItemSet("\"VeilColor=white\""),new ItemSet("\"GillSpace=close\""),0.8148695,0.8354366,0.9963428));
		rulesTest.add(new Rule(new ItemSet("\"GillAttached=free\""),new ItemSet("\"GillSpace=close\""),0.8126539,0.8342178,0.9948893));
		
		for(Rule rule: rules) {
			assertEquals(rule.toString(),true, rulesTest.contains(rule));
		}
			
	}
	
	@Test
	public void testgetRulesChangeLift() {
		Apriori apriori = new Apriori(items);
		apriori.setMinlen(2);
		apriori.setMaxlen(2);
		apriori.setSupport(0.6);
		apriori.setConfidence(0.5);
		apriori.setLift(x -> x < 1);
		List<Rule> rules = apriori.getRules();
		assertEquals("Length are not the same",16   , rules.size());
		Set<Rule> rulesTest = new HashSet<>();
		rulesTest.add(new Rule(new ItemSet("\"RingNumber=one\""),new ItemSet("\"VeilColor=white\""),0.8970950,0.9732906,0.9978562));
		rulesTest.add(new Rule(new ItemSet("\"GillSpace=close\""),new ItemSet("\"VeilColor=white\""),0.8148695,0.9718144,0.9963428));
		rulesTest.add(new Rule(new ItemSet("\"GillSpace=close\""),new ItemSet("\"GillAttached=free\""),0.8126539,0.9691720,0.9948893));
		rulesTest.add(new Rule(new ItemSet("\"GillSize=broad\""),new ItemSet("\"VeilColor=white\""),0.6671590,0.9657876,0.9901639));
		rulesTest.add(new Rule(new ItemSet("\"SurfaceAboveRing=smooth\""),new ItemSet("\"GillAttached=free\""),0.6134909,0.9629057,0.9884567));
		rulesTest.add(new Rule(new ItemSet("\"SurfaceAboveRing=smooth\""),new ItemSet("\"VeilColor=white\""),0.6134909,0.9629057,0.9872092));
		rulesTest.add(new Rule(new ItemSet("\"GillSize=broad\""),new ItemSet("\"GillAttached=free\""),0.6649434,0.9625802,0.9881225));
		rulesTest.add(new Rule(new ItemSet("\"VeilColor=white\""),new ItemSet("\"RingNumber=one\""),0.8970950,0.9197375,0.9978562));
		rulesTest.add(new Rule(new ItemSet("\"GillSize=broad\""),new ItemSet("\"RingNumber=one\""),0.6125062,0.8866714,0.9619817));
		rulesTest.add(new Rule(new ItemSet("\"VeilColor=white\""),new ItemSet("\"GillSpace=close\""),0.8148695,0.8354366,0.9963428));
		rulesTest.add(new Rule(new ItemSet("\"GillAttached=free\""),new ItemSet("\"GillSpace=close\""),0.8126539,0.8342178,0.9948893));
		rulesTest.add(new Rule(new ItemSet("\"VeilColor=white\""),new ItemSet("\"GillSize=broad\""),0.6671590,0.6839980,0.9901639));
		rulesTest.add(new Rule(new ItemSet("\"GillAttached=free\""),new ItemSet("\"GillSize=broad\""),0.6649434,0.6825878,0.9881225));
		rulesTest.add(new Rule(new ItemSet("\"RingNumber=one\""),new ItemSet("\"GillSize=broad\""),0.6125062,0.6645299,0.9619817));
		rulesTest.add(new Rule(new ItemSet("\"GillAttached=free\""),new ItemSet("\"SurfaceAboveRing=smooth\""),0.6134909,0.6297700,0.9884567));
		rulesTest.add(new Rule(new ItemSet("\"VeilColor=white\""),new ItemSet("\"SurfaceAboveRing=smooth\""),0.6134909,0.6289753,0.9872092));

		
		for(Rule rule: rules) {
			assertEquals(rule.toString(),true, rulesTest.contains(rule));
		}
			
	}
	
	@Test
	public void testgetRulesChangeLiftTwice() {
		Apriori apriori = new Apriori(items);
		apriori.setMinlen(2);
		apriori.setMaxlen(2);
		apriori.setSupport(0.6);
		apriori.setConfidence(0.5);
		apriori.setLift(x -> x < 1);
		List<Rule> rules = apriori.getRules();
		
		apriori.setLift(x -> x >= 1);
		rules = apriori.getRules();
		assertEquals("Length are not the same",20   , rules.size());
		Set<Rule> rulesTest = new HashSet<>();
		rulesTest.add(new Rule(new ItemSet("\"SurfaceBelowRing=smooth\""),new ItemSet("\"VeilType=partial\""),0.6075825,1.0000000,1.000000));
		rulesTest.add(new Rule(new ItemSet("\"SurfaceAboveRing=smooth\""),new ItemSet("\"VeilType=partial\""),0.6371246,1.0000000,1.000000));
		rulesTest.add(new Rule(new ItemSet("\"GillSize=broad\""),new ItemSet("\"VeilType=partial\""),0.6907927,1.0000000,1.000000));
		rulesTest.add(new Rule(new ItemSet("\"GillSpace=close\""),new ItemSet("\"VeilType=partial\""),0.8385032,1.0000000,1.000000));
		rulesTest.add(new Rule(new ItemSet("\"RingNumber=one\""),new ItemSet("\"VeilType=partial\""),0.9217134,1.0000000,1.000000));
		rulesTest.add(new Rule(new ItemSet("\"GillAttached=free\""),new ItemSet("\"VeilType=partial\""),0.9741507,1.0000000,1.000000));
		rulesTest.add(new Rule(new ItemSet("\"VeilColor=white\""),new ItemSet("\"VeilType=partial\""),0.9753816,1.0000000,1.000000));
		rulesTest.add(new Rule(new ItemSet("\"GillAttached=free\""),new ItemSet("\"VeilColor=white\""),0.9731659,0.9989891,1.024203));
		rulesTest.add(new Rule(new ItemSet("\"VeilColor=white\""),new ItemSet("\"GillAttached=free\""),0.9731659,0.9977284,1.024203));
		rulesTest.add(new Rule(new ItemSet("\"VeilType=partial\""),new ItemSet("\"VeilColor=white\""),0.9753816,0.9753816,1.000000));
		rulesTest.add(new Rule(new ItemSet("\"RingNumber=one\""),new ItemSet("\"GillAttached=free\""),0.8980798,0.9743590,1.000214));
		rulesTest.add(new Rule(new ItemSet("\"VeilType=partial\""),new ItemSet("\"GillAttached=free\""),0.9741507,0.9741507,1.000000));
		rulesTest.add(new Rule(new ItemSet("\"GillSpace=close\""),new ItemSet("\"RingNumber=one\""),0.7956672,0.9489137,1.029511));
		rulesTest.add(new Rule(new ItemSet("\"GillAttached=free\""),new ItemSet("\"RingNumber=one\""),0.8980798,0.9219105,1.000214));
		rulesTest.add(new Rule(new ItemSet("\"VeilType=partial\""),new ItemSet("\"RingNumber=one\""),0.9217134,0.9217134,1.000000));
		rulesTest.add(new Rule(new ItemSet("\"RingNumber=one\""),new ItemSet("\"GillSpace=close\""),0.7956672,0.8632479,1.029511));
		rulesTest.add(new Rule(new ItemSet("\"VeilType=partial\""),new ItemSet("\"GillSpace=close\""),0.8385032,0.8385032,1.000000));
		rulesTest.add(new Rule(new ItemSet("\"VeilType=partial\""),new ItemSet("\"GillSize=broad\""),0.6907927,0.6907927,1.000000));
		rulesTest.add(new Rule(new ItemSet("\"VeilType=partial\""),new ItemSet("\"SurfaceAboveRing=smooth\""),0.6371246,0.6371246,1.000000));
		rulesTest.add(new Rule(new ItemSet("\"VeilType=partial\""),new ItemSet("\"SurfaceBelowRing=smooth\""),0.6075825,0.6075825,1.000000));
		
		for(Rule rule: rules) {
			assertEquals(rule.toString(),true, rulesTest.contains(rule));
		}
			
	}
	
	@Test
	public void testgetRulesMaxLen3() {
		Apriori apriori = new Apriori(items);
		apriori.setMinlen(2);
		apriori.setMaxlen(3);
		apriori.setSupport(0.9);
		apriori.setConfidence(0.8);
		List<Rule> rules = apriori.getRules();
		
		assertEquals("Length are not the same",11   , rules.size());
		Set<Rule> rulesTest = new HashSet<>();
		rulesTest.add(new Rule(new ItemSet("\"RingNumber=one\""),new ItemSet("\"VeilType=partial\""),0.9217134,1.0000000,1.000000));
		rulesTest.add(new Rule(new ItemSet("\"GillAttached=free\""),new ItemSet("\"VeilType=partial\""),0.9741507,1.0000000,1.000000));
		rulesTest.add(new Rule(new ItemSet("\"VeilColor=white\""),new ItemSet("\"VeilType=partial\""),0.9753816,1.0000000,1.000000));
		rulesTest.add(new Rule(new ItemSet("\"GillAttached=free\"","\"VeilColor=white\""),new ItemSet("\"VeilType=partial\""),0.9731659,1.0000000,1.000000));
		rulesTest.add(new Rule(new ItemSet("\"GillAttached=free\""),new ItemSet("\"VeilColor=white\""),0.9731659,0.9989891,1.024203));
		rulesTest.add(new Rule(new ItemSet("\"GillAttached=free\"","\"VeilType=partial\""),new ItemSet("\"VeilColor=white\""),0.9731659,0.9989891,1.024203));
		rulesTest.add(new Rule(new ItemSet("\"VeilColor=white\""),new ItemSet("\"GillAttached=free\""),0.9731659,0.9977284,1.024203));
		rulesTest.add(new Rule(new ItemSet("\"VeilType=partial\"","\"VeilColor=white\""),new ItemSet("\"GillAttached=free\""),0.9731659,0.9977284,1.024203));
		rulesTest.add(new Rule(new ItemSet("\"VeilType=partial\""),new ItemSet("\"VeilColor=white\""),0.9753816,0.9753816,1.000000));
		rulesTest.add(new Rule(new ItemSet("\"VeilType=partial\""),new ItemSet("\"GillAttached=free\""),0.9741507,0.9741507,1.000000));
		rulesTest.add(new Rule(new ItemSet("\"VeilType=partial\""),new ItemSet("\"RingNumber=one\""),0.9217134,0.9217134,1.000000));
		
		for(Rule rule: rules) {
			assertEquals(rule.toString(),true, rulesTest.contains(rule));
		}
			
	}
	
	@Test
	public void testgetRulesMaxLen6() {
		Apriori apriori = new Apriori(items);
		apriori.setMinlen(5);
		apriori.setMaxlen(6);
		apriori.setSupport(0.5);
		apriori.setConfidence(0.8);
		List<Rule> rules = apriori.getRules();
		
		assertEquals("Length are not the same",33   , rules.size());
		Set<Rule> rulesTest = new HashSet<>();
		rulesTest.add(new Rule(new ItemSet("\"GillAttached=free\"","\"StalkShape=tapering\"","\"VeilColor=white\"","\"RingNumber=one\""),new ItemSet("\"VeilType=partial\""),0.567208271787297,1,1));
		rulesTest.add(new Rule(new ItemSet("\"GillAttached=free\"","\"StalkShape=tapering\"","\"VeilType=partial\"","\"RingNumber=one\""),new ItemSet("\"VeilColor=white\""),0.567208271787297,1,1.02523977788995));
		rulesTest.add(new Rule(new ItemSet("\"StalkShape=tapering\"","\"VeilType=partial\"","\"VeilColor=white\"","\"RingNumber=one\""),new ItemSet("\"GillAttached=free\""),0.567208271787297,1,1.02653525398029));
		rulesTest.add(new Rule(new ItemSet("\"GillAttached=free\"","\"StalkShape=tapering\"","\"VeilType=partial\"","\"VeilColor=white\""),new ItemSet("\"RingNumber=one\""),0.567208271787297,1,1.0849358974359));
		rulesTest.add(new Rule(new ItemSet("\"Bruises=no\"","\"GillAttached=free\"","\"VeilColor=white\"","\"RingNumber=one\""),new ItemSet("\"VeilType=partial\""),0.517971442639094,1,1));
		rulesTest.add(new Rule(new ItemSet("\"Bruises=no\"","\"VeilType=partial\"","\"VeilColor=white\"","\"RingNumber=one\""),new ItemSet("\"GillAttached=free\""),0.517971442639094,1,1.02653525398029));
		rulesTest.add(new Rule(new ItemSet("\"GillAttached=free\"","\"SurfaceBelowRing=smooth\"","\"VeilColor=white\"","\"RingNumber=one\""),new ItemSet("\"VeilType=partial\""),0.529788281634663,1,1));
		rulesTest.add(new Rule(new ItemSet("\"GillAttached=free\"","\"SurfaceBelowRing=smooth\"","\"VeilType=partial\"","\"RingNumber=one\""),new ItemSet("\"VeilColor=white\""),0.529788281634663,1,1.02523977788995));
		rulesTest.add(new Rule(new ItemSet("\"SurfaceBelowRing=smooth\"","\"VeilType=partial\"","\"VeilColor=white\"","\"RingNumber=one\""),new ItemSet("\"GillAttached=free\""),0.529788281634663,1,1.02653525398029));
		rulesTest.add(new Rule(new ItemSet("\"GillAttached=free\"","\"GillSpace=close\"","\"SurfaceAboveRing=smooth\"","\"VeilColor=white\""),new ItemSet("\"VeilType=partial\""),0.520925652387986,1,1));
		rulesTest.add(new Rule(new ItemSet("\"GillAttached=free\"","\"GillSpace=close\"","\"SurfaceAboveRing=smooth\"","\"VeilType=partial\""),new ItemSet("\"VeilColor=white\""),0.520925652387986,1,1.02523977788995));
		rulesTest.add(new Rule(new ItemSet("\"GillSpace=close\"","\"SurfaceAboveRing=smooth\"","\"VeilType=partial\"","\"VeilColor=white\""),new ItemSet("\"GillAttached=free\""),0.520925652387986,1,1.02653525398029));
		rulesTest.add(new Rule(new ItemSet("\"GillAttached=free\"","\"SurfaceAboveRing=smooth\"","\"VeilColor=white\"","\"RingNumber=one\""),new ItemSet("\"VeilType=partial\""),0.559330379123584,1,1));
		rulesTest.add(new Rule(new ItemSet("\"GillAttached=free\"","\"SurfaceAboveRing=smooth\"","\"VeilType=partial\"","\"RingNumber=one\""),new ItemSet("\"VeilColor=white\""),0.559330379123584,1,1.02523977788995));
		rulesTest.add(new Rule(new ItemSet("\"SurfaceAboveRing=smooth\"","\"VeilType=partial\"","\"VeilColor=white\"","\"RingNumber=one\""),new ItemSet("\"GillAttached=free\""),0.559330379123584,1,1.02653525398029));
		rulesTest.add(new Rule(new ItemSet("\"GillAttached=free\"","\"GillSpace=close\"","\"GillSize=broad\"","\"VeilColor=white\""),new ItemSet("\"VeilType=partial\""),0.534958148695224,1,1));
		rulesTest.add(new Rule(new ItemSet("\"GillAttached=free\"","\"GillSpace=close\"","\"GillSize=broad\"","\"VeilType=partial\""),new ItemSet("\"VeilColor=white\""),0.534958148695224,1,1.02523977788995));
		rulesTest.add(new Rule(new ItemSet("\"GillAttached=free\"","\"GillSize=broad\"","\"VeilColor=white\"","\"RingNumber=one\""),new ItemSet("\"VeilType=partial\""),0.588872476612506,1,1));
		rulesTest.add(new Rule(new ItemSet("\"GillAttached=free\"","\"GillSize=broad\"","\"VeilType=partial\"","\"RingNumber=one\""),new ItemSet("\"VeilColor=white\""),0.588872476612506,1,1.02523977788995));
		rulesTest.add(new Rule(new ItemSet("\"GillSize=broad\"","\"VeilType=partial\"","\"VeilColor=white\"","\"RingNumber=one\""),new ItemSet("\"GillAttached=free\""),0.588872476612506,1,1.02653525398029));
		rulesTest.add(new Rule(new ItemSet("\"GillAttached=free\"","\"GillSpace=close\"","\"VeilColor=white\"","\"RingNumber=one\""),new ItemSet("\"VeilType=partial\""),0.772033481043821,1,1));
		rulesTest.add(new Rule(new ItemSet("\"GillAttached=free\"","\"GillSpace=close\"","\"VeilType=partial\"","\"RingNumber=one\""),new ItemSet("\"VeilColor=white\""),0.772033481043821,1,1.02523977788995));
		rulesTest.add(new Rule(new ItemSet("\"GillSpace=close\"","\"VeilType=partial\"","\"VeilColor=white\"","\"RingNumber=one\""),new ItemSet("\"GillAttached=free\""),0.772033481043821,1,1.02653525398029));
		rulesTest.add(new Rule(new ItemSet("\"Bruises=no\"","\"GillAttached=free\"","\"VeilType=partial\"","\"RingNumber=one\""),new ItemSet("\"VeilColor=white\""),0.517971442639094,0.998102466793169,1.02329435136644));
		rulesTest.add(new Rule(new ItemSet("\"GillSpace=close\"","\"GillSize=broad\"","\"VeilType=partial\"","\"VeilColor=white\""),new ItemSet("\"GillAttached=free\""),0.534958148695224,0.995875343721357,1.02230114889971));
		rulesTest.add(new Rule(new ItemSet("\"GillAttached=free\"","\"GillSpace=close\"","\"VeilType=partial\"","\"VeilColor=white\""),new ItemSet("\"RingNumber=one\""),0.772033481043821,0.950015146925174,1.03070553600696));
		rulesTest.add(new Rule(new ItemSet("\"Bruises=no\"","\"GillAttached=free\"","\"VeilType=partial\"","\"VeilColor=white\""),new ItemSet("\"RingNumber=one\""),0.517971442639094,0.928918322295806,1.00781683364465));
		rulesTest.add(new Rule(new ItemSet("\"GillAttached=free\"","\"SurfaceAboveRing=smooth\"","\"VeilType=partial\"","\"VeilColor=white\""),new ItemSet("\"RingNumber=one\""),0.559330379123584,0.911717495987159,0.989155039716837));
		rulesTest.add(new Rule(new ItemSet("\"GillAttached=free\"","\"SurfaceBelowRing=smooth\"","\"VeilType=partial\"","\"VeilColor=white\""),new ItemSet("\"RingNumber=one\""),0.529788281634663,0.907251264755481,0.98430946512734));
		rulesTest.add(new Rule(new ItemSet("\"GillAttached=free\"","\"GillSize=broad\"","\"VeilType=partial\"","\"VeilColor=white\""),new ItemSet("\"RingNumber=one\""),0.588872476612506,0.885597926693817,0.960816981364927));
		rulesTest.add(new Rule(new ItemSet("\"GillAttached=free\"","\"VeilType=partial\"","\"VeilColor=white\"","\"RingNumber=one\""),new ItemSet("\"GillSpace=close\""),0.772033481043821,0.860592755214051,1.02634403161464));
		rulesTest.add(new Rule(new ItemSet("\"GillAttached=free\"","\"SurfaceAboveRing=smooth\"","\"VeilType=partial\"","\"VeilColor=white\""),new ItemSet("\"GillSpace=close\""),0.520925652387986,0.849117174959872,1.01265823977892));
		rulesTest.add(new Rule(new ItemSet("\"GillAttached=free\"","\"GillSize=broad\"","\"VeilType=partial\"","\"VeilColor=white\""),new ItemSet("\"GillSpace=close\""),0.534958148695224,0.804516845612736,0.959467829383128));
		
		for(Rule rule: rules) {
			assertEquals(rule.toString(),true, rulesTest.contains(rule));
		}
			
	}
	
	@Test
	public void testgetRulesLeftAny() {
		Apriori apriori = new Apriori(items);
		apriori.setMinlen(5);
		apriori.setMaxlen(6);
		apriori.setSupport(0.5);
		apriori.setConfidence(0.8);
		apriori.filterStringLeft = "any";
		Set<String> left = new HashSet<>();
		left.add("\"GillAttached=free\"");
		left.add("\"GillSize=broad\"");
		apriori.leftSide = left;
		List<Rule> rules = apriori.getRules();
		
		assertEquals("Length are not the same",27   , rules.size());
		Set<Rule> rulesTest = new HashSet<>();
		rulesTest.add(new Rule(new ItemSet("\"GillAttached=free\"","\"StalkShape=tapering\"","\"VeilColor=white\"","\"RingNumber=one\""),new ItemSet("\"VeilType=partial\""),0.567208271787297,1,1));
		rulesTest.add(new Rule(new ItemSet("\"GillAttached=free\"","\"StalkShape=tapering\"","\"VeilType=partial\"","\"RingNumber=one\""),new ItemSet("\"VeilColor=white\""),0.567208271787297,1,1.02523977788995));
		rulesTest.add(new Rule(new ItemSet("\"GillAttached=free\"","\"StalkShape=tapering\"","\"VeilType=partial\"","\"VeilColor=white\""),new ItemSet("\"RingNumber=one\""),0.567208271787297,1,1.0849358974359));
		rulesTest.add(new Rule(new ItemSet("\"Bruises=no\"","\"GillAttached=free\"","\"VeilColor=white\"","\"RingNumber=one\""),new ItemSet("\"VeilType=partial\""),0.517971442639094,1,1));
		rulesTest.add(new Rule(new ItemSet("\"GillAttached=free\"","\"SurfaceBelowRing=smooth\"","\"VeilColor=white\"","\"RingNumber=one\""),new ItemSet("\"VeilType=partial\""),0.529788281634663,1,1));
		rulesTest.add(new Rule(new ItemSet("\"GillAttached=free\"","\"SurfaceBelowRing=smooth\"","\"VeilType=partial\"","\"RingNumber=one\""),new ItemSet("\"VeilColor=white\""),0.529788281634663,1,1.02523977788995));
		rulesTest.add(new Rule(new ItemSet("\"GillAttached=free\"","\"GillSpace=close\"","\"SurfaceAboveRing=smooth\"","\"VeilColor=white\""),new ItemSet("\"VeilType=partial\""),0.520925652387986,1,1));
		rulesTest.add(new Rule(new ItemSet("\"GillAttached=free\"","\"GillSpace=close\"","\"SurfaceAboveRing=smooth\"","\"VeilType=partial\""),new ItemSet("\"VeilColor=white\""),0.520925652387986,1,1.02523977788995));
		rulesTest.add(new Rule(new ItemSet("\"GillAttached=free\"","\"SurfaceAboveRing=smooth\"","\"VeilColor=white\"","\"RingNumber=one\""),new ItemSet("\"VeilType=partial\""),0.559330379123584,1,1));
		rulesTest.add(new Rule(new ItemSet("\"GillAttached=free\"","\"SurfaceAboveRing=smooth\"","\"VeilType=partial\"","\"RingNumber=one\""),new ItemSet("\"VeilColor=white\""),0.559330379123584,1,1.02523977788995));
		rulesTest.add(new Rule(new ItemSet("\"GillAttached=free\"","\"GillSpace=close\"","\"GillSize=broad\"","\"VeilColor=white\""),new ItemSet("\"VeilType=partial\""),0.534958148695224,1,1));
		rulesTest.add(new Rule(new ItemSet("\"GillAttached=free\"","\"GillSpace=close\"","\"GillSize=broad\"","\"VeilType=partial\""),new ItemSet("\"VeilColor=white\""),0.534958148695224,1,1.02523977788995));
		rulesTest.add(new Rule(new ItemSet("\"GillAttached=free\"","\"GillSize=broad\"","\"VeilColor=white\"","\"RingNumber=one\""),new ItemSet("\"VeilType=partial\""),0.588872476612506,1,1));
		rulesTest.add(new Rule(new ItemSet("\"GillAttached=free\"","\"GillSize=broad\"","\"VeilType=partial\"","\"RingNumber=one\""),new ItemSet("\"VeilColor=white\""),0.588872476612506,1,1.02523977788995));
		rulesTest.add(new Rule(new ItemSet("\"GillSize=broad\"","\"VeilType=partial\"","\"VeilColor=white\"","\"RingNumber=one\""),new ItemSet("\"GillAttached=free\""),0.588872476612506,1,1.02653525398029));
		rulesTest.add(new Rule(new ItemSet("\"GillAttached=free\"","\"GillSpace=close\"","\"VeilColor=white\"","\"RingNumber=one\""),new ItemSet("\"VeilType=partial\""),0.772033481043821,1,1));
		rulesTest.add(new Rule(new ItemSet("\"GillAttached=free\"","\"GillSpace=close\"","\"VeilType=partial\"","\"RingNumber=one\""),new ItemSet("\"VeilColor=white\""),0.772033481043821,1,1.02523977788995));
		rulesTest.add(new Rule(new ItemSet("\"Bruises=no\"","\"GillAttached=free\"","\"VeilType=partial\"","\"RingNumber=one\""),new ItemSet("\"VeilColor=white\""),0.517971442639094,0.998102466793169,1.02329435136644));
		rulesTest.add(new Rule(new ItemSet("\"GillSpace=close\"","\"GillSize=broad\"","\"VeilType=partial\"","\"VeilColor=white\""),new ItemSet("\"GillAttached=free\""),0.534958148695224,0.995875343721357,1.02230114889971));
		rulesTest.add(new Rule(new ItemSet("\"GillAttached=free\"","\"GillSpace=close\"","\"VeilType=partial\"","\"VeilColor=white\""),new ItemSet("\"RingNumber=one\""),0.772033481043821,0.950015146925174,1.03070553600696));
		rulesTest.add(new Rule(new ItemSet("\"Bruises=no\"","\"GillAttached=free\"","\"VeilType=partial\"","\"VeilColor=white\""),new ItemSet("\"RingNumber=one\""),0.517971442639094,0.928918322295806,1.00781683364465));
		rulesTest.add(new Rule(new ItemSet("\"GillAttached=free\"","\"SurfaceAboveRing=smooth\"","\"VeilType=partial\"","\"VeilColor=white\""),new ItemSet("\"RingNumber=one\""),0.559330379123584,0.911717495987159,0.989155039716837));
		rulesTest.add(new Rule(new ItemSet("\"GillAttached=free\"","\"SurfaceBelowRing=smooth\"","\"VeilType=partial\"","\"VeilColor=white\""),new ItemSet("\"RingNumber=one\""),0.529788281634663,0.907251264755481,0.98430946512734));
		rulesTest.add(new Rule(new ItemSet("\"GillAttached=free\"","\"GillSize=broad\"","\"VeilType=partial\"","\"VeilColor=white\""),new ItemSet("\"RingNumber=one\""),0.588872476612506,0.885597926693817,0.960816981364927));
		rulesTest.add(new Rule(new ItemSet("\"GillAttached=free\"","\"VeilType=partial\"","\"VeilColor=white\"","\"RingNumber=one\""),new ItemSet("\"GillSpace=close\""),0.772033481043821,0.860592755214051,1.02634403161464));
		rulesTest.add(new Rule(new ItemSet("\"GillAttached=free\"","\"SurfaceAboveRing=smooth\"","\"VeilType=partial\"","\"VeilColor=white\""),new ItemSet("\"GillSpace=close\""),0.520925652387986,0.849117174959872,1.01265823977892));
		rulesTest.add(new Rule(new ItemSet("\"GillAttached=free\"","\"GillSize=broad\"","\"VeilType=partial\"","\"VeilColor=white\""),new ItemSet("\"GillSpace=close\""),0.534958148695224,0.804516845612736,0.959467829383128));
		
		for(Rule rule: rules) {
			assertEquals(rule.toString(),true, rulesTest.contains(rule));
		}
			
	}
	
	@Test
	public void testgetRulesLeftAll() {
		Apriori apriori = new Apriori(items);
		apriori.setMinlen(5);
		apriori.setMaxlen(6);
		apriori.setSupport(0.5);
		apriori.setConfidence(0.8);
		apriori.filterStringLeft = "all";
		Set<String> left = new HashSet<>();
		left.add("\"GillAttached=free\"");
		left.add("\"GillSize=broad\"");
		apriori.leftSide = left;
		List<Rule> rules = apriori.getRules();
		
		assertEquals("Length are not the same", 6 , rules.size());
		Set<Rule> rulesTest = new HashSet<>();
		rulesTest.add(new Rule(new ItemSet("\"GillAttached=free\"","\"GillSpace=close\"","\"GillSize=broad\"","\"VeilColor=white\""),new ItemSet("\"VeilType=partial\""),0.534958148695224,1,1));
		rulesTest.add(new Rule(new ItemSet("\"GillAttached=free\"","\"GillSpace=close\"","\"GillSize=broad\"","\"VeilType=partial\""),new ItemSet("\"VeilColor=white\""),0.534958148695224,1,1.02523977788995));
		rulesTest.add(new Rule(new ItemSet("\"GillAttached=free\"","\"GillSize=broad\"","\"VeilColor=white\"","\"RingNumber=one\""),new ItemSet("\"VeilType=partial\""),0.588872476612506,1,1));
		rulesTest.add(new Rule(new ItemSet("\"GillAttached=free\"","\"GillSize=broad\"","\"VeilType=partial\"","\"RingNumber=one\""),new ItemSet("\"VeilColor=white\""),0.588872476612506,1,1.02523977788995));
		rulesTest.add(new Rule(new ItemSet("\"GillAttached=free\"","\"GillSize=broad\"","\"VeilType=partial\"","\"VeilColor=white\""),new ItemSet("\"RingNumber=one\""),0.588872476612506,0.885597926693817,0.960816981364927));
		rulesTest.add(new Rule(new ItemSet("\"GillAttached=free\"","\"GillSize=broad\"","\"VeilType=partial\"","\"VeilColor=white\""),new ItemSet("\"GillSpace=close\""),0.534958148695224,0.804516845612736,0.959467829383128));
		
		for(Rule rule: rules) {
			assertEquals(rule.toString(),true, rulesTest.contains(rule));
		}
			
	}
	
	@Test
	public void testgetRulesLeftNone() {
		Apriori apriori = new Apriori(items);
		apriori.setMinlen(5);
		apriori.setMaxlen(6);
		apriori.setSupport(0.5);
		apriori.setConfidence(0.8);
		apriori.filterStringLeft = "none";
		Set<String> left = new HashSet<>();
		left.add("\"GillAttached=free\"");
		left.add("\"GillSize=broad\"");
		apriori.leftSide = left;
		List<Rule> rules = apriori.getRules();
		
		assertEquals("Length are not the same", 6 , rules.size());
		Set<Rule> rulesTest = new HashSet<>();
		rulesTest.add(new Rule(new ItemSet("\"StalkShape=tapering\"","\"VeilType=partial\"","\"VeilColor=white\"","\"RingNumber=one\""),new ItemSet("\"GillAttached=free\""),0.567208271787297,1,1.02653525398029));
		rulesTest.add(new Rule(new ItemSet("\"Bruises=no\"","\"VeilType=partial\"","\"VeilColor=white\"","\"RingNumber=one\""),new ItemSet("\"GillAttached=free\""),0.517971442639094,1,1.02653525398029));
		rulesTest.add(new Rule(new ItemSet("\"SurfaceBelowRing=smooth\"","\"VeilType=partial\"","\"VeilColor=white\"","\"RingNumber=one\""),new ItemSet("\"GillAttached=free\""),0.529788281634663,1,1.02653525398029));
		rulesTest.add(new Rule(new ItemSet("\"GillSpace=close\"","\"SurfaceAboveRing=smooth\"","\"VeilType=partial\"","\"VeilColor=white\""),new ItemSet("\"GillAttached=free\""),0.520925652387986,1,1.02653525398029));
		rulesTest.add(new Rule(new ItemSet("\"SurfaceAboveRing=smooth\"","\"VeilType=partial\"","\"VeilColor=white\"","\"RingNumber=one\""),new ItemSet("\"GillAttached=free\""),0.559330379123584,1,1.02653525398029));
		rulesTest.add(new Rule(new ItemSet("\"GillSpace=close\"","\"VeilType=partial\"","\"VeilColor=white\"","\"RingNumber=one\""),new ItemSet("\"GillAttached=free\""),0.772033481043821,1,1.02653525398029));
		for(Rule rule: rules) {
			assertEquals(rule.toString(),true, rulesTest.contains(rule));
		}
			
	}
	
	@Test
	public void testgetRulesRightAny() {
		Apriori apriori = new Apriori(items);
		apriori.setMinlen(5);
		apriori.setMaxlen(6);
		apriori.setSupport(0.5);
		apriori.setConfidence(0.8);
		apriori.filterStringRight = "any";
		Set<String> right = new HashSet<>();
		right.add("\"GillAttached=free\"");
		right.add("\"VeilType=partial\"");
		apriori.rightSide = right;
		List<Rule> rules = apriori.getRules();
		
		assertEquals("Length are not the same", 16 , rules.size());
		Set<Rule> rulesTest = new HashSet<>();
		rulesTest.add(new Rule(new ItemSet("\"GillAttached=free\"","\"StalkShape=tapering\"","\"VeilColor=white\"","\"RingNumber=one\""),new ItemSet("\"VeilType=partial\""),0.567208271787297,1,1));
		rulesTest.add(new Rule(new ItemSet("\"StalkShape=tapering\"","\"VeilType=partial\"","\"VeilColor=white\"","\"RingNumber=one\""),new ItemSet("\"GillAttached=free\""),0.567208271787297,1,1.02653525398029));
		rulesTest.add(new Rule(new ItemSet("\"Bruises=no\"","\"GillAttached=free\"","\"VeilColor=white\"","\"RingNumber=one\""),new ItemSet("\"VeilType=partial\""),0.517971442639094,1,1));
		rulesTest.add(new Rule(new ItemSet("\"Bruises=no\"","\"VeilType=partial\"","\"VeilColor=white\"","\"RingNumber=one\""),new ItemSet("\"GillAttached=free\""),0.517971442639094,1,1.02653525398029));
		rulesTest.add(new Rule(new ItemSet("\"GillAttached=free\"","\"SurfaceBelowRing=smooth\"","\"VeilColor=white\"","\"RingNumber=one\""),new ItemSet("\"VeilType=partial\""),0.529788281634663,1,1));
		rulesTest.add(new Rule(new ItemSet("\"SurfaceBelowRing=smooth\"","\"VeilType=partial\"","\"VeilColor=white\"","\"RingNumber=one\""),new ItemSet("\"GillAttached=free\""),0.529788281634663,1,1.02653525398029));
		rulesTest.add(new Rule(new ItemSet("\"GillAttached=free\"","\"GillSpace=close\"","\"SurfaceAboveRing=smooth\"","\"VeilColor=white\""),new ItemSet("\"VeilType=partial\""),0.520925652387986,1,1));
		rulesTest.add(new Rule(new ItemSet("\"GillSpace=close\"","\"SurfaceAboveRing=smooth\"","\"VeilType=partial\"","\"VeilColor=white\""),new ItemSet("\"GillAttached=free\""),0.520925652387986,1,1.02653525398029));
		rulesTest.add(new Rule(new ItemSet("\"GillAttached=free\"","\"SurfaceAboveRing=smooth\"","\"VeilColor=white\"","\"RingNumber=one\""),new ItemSet("\"VeilType=partial\""),0.559330379123584,1,1));
		rulesTest.add(new Rule(new ItemSet("\"SurfaceAboveRing=smooth\"","\"VeilType=partial\"","\"VeilColor=white\"","\"RingNumber=one\""),new ItemSet("\"GillAttached=free\""),0.559330379123584,1,1.02653525398029));
		rulesTest.add(new Rule(new ItemSet("\"GillAttached=free\"","\"GillSpace=close\"","\"GillSize=broad\"","\"VeilColor=white\""),new ItemSet("\"VeilType=partial\""),0.534958148695224,1,1));
		rulesTest.add(new Rule(new ItemSet("\"GillAttached=free\"","\"GillSize=broad\"","\"VeilColor=white\"","\"RingNumber=one\""),new ItemSet("\"VeilType=partial\""),0.588872476612506,1,1));
		rulesTest.add(new Rule(new ItemSet("\"GillSize=broad\"","\"VeilType=partial\"","\"VeilColor=white\"","\"RingNumber=one\""),new ItemSet("\"GillAttached=free\""),0.588872476612506,1,1.02653525398029));
		rulesTest.add(new Rule(new ItemSet("\"GillAttached=free\"","\"GillSpace=close\"","\"VeilColor=white\"","\"RingNumber=one\""),new ItemSet("\"VeilType=partial\""),0.772033481043821,1,1));
		rulesTest.add(new Rule(new ItemSet("\"GillSpace=close\"","\"VeilType=partial\"","\"VeilColor=white\"","\"RingNumber=one\""),new ItemSet("\"GillAttached=free\""),0.772033481043821,1,1.02653525398029));
		rulesTest.add(new Rule(new ItemSet("\"GillSpace=close\"","\"GillSize=broad\"","\"VeilType=partial\"","\"VeilColor=white\""),new ItemSet("\"GillAttached=free\""),0.534958148695224,0.995875343721357,1.02230114889971));
		
		for(Rule rule: rules) {
			assertEquals(rule.toString(),true, rulesTest.contains(rule));
		}
			
	}
	
	@Test
	public void testgetRulesRightNone() {
		Apriori apriori = new Apriori(items);
		apriori.setMinlen(5);
		apriori.setMaxlen(6);
		apriori.setSupport(0.5);
		apriori.setConfidence(0.8);
		apriori.filterStringRight = "none";
		Set<String> right = new HashSet<>();
		right.add("\"GillAttached=free\"");
		right.add("\"VeilType=partial\"");
		apriori.rightSide = right;
		List<Rule> rules = apriori.getRules();
		
		assertEquals("Length are not the same", 17 , rules.size());
		Set<Rule> rulesTest = new HashSet<>();
		rulesTest.add(new Rule(new ItemSet("\"GillAttached=free\"","\"StalkShape=tapering\"","\"VeilType=partial\"","\"RingNumber=one\""),new ItemSet("\"VeilColor=white\""),0.567208271787297,1,1.02523977788995));
		rulesTest.add(new Rule(new ItemSet("\"GillAttached=free\"","\"StalkShape=tapering\"","\"VeilType=partial\"","\"VeilColor=white\""),new ItemSet("\"RingNumber=one\""),0.567208271787297,1,1.0849358974359));
		rulesTest.add(new Rule(new ItemSet("\"GillAttached=free\"","\"SurfaceBelowRing=smooth\"","\"VeilType=partial\"","\"RingNumber=one\""),new ItemSet("\"VeilColor=white\""),0.529788281634663,1,1.02523977788995));
		rulesTest.add(new Rule(new ItemSet("\"GillAttached=free\"","\"GillSpace=close\"","\"SurfaceAboveRing=smooth\"","\"VeilType=partial\""),new ItemSet("\"VeilColor=white\""),0.520925652387986,1,1.02523977788995));
		rulesTest.add(new Rule(new ItemSet("\"GillAttached=free\"","\"SurfaceAboveRing=smooth\"","\"VeilType=partial\"","\"RingNumber=one\""),new ItemSet("\"VeilColor=white\""),0.559330379123584,1,1.02523977788995));
		rulesTest.add(new Rule(new ItemSet("\"GillAttached=free\"","\"GillSpace=close\"","\"GillSize=broad\"","\"VeilType=partial\""),new ItemSet("\"VeilColor=white\""),0.534958148695224,1,1.02523977788995));
		rulesTest.add(new Rule(new ItemSet("\"GillAttached=free\"","\"GillSize=broad\"","\"VeilType=partial\"","\"RingNumber=one\""),new ItemSet("\"VeilColor=white\""),0.588872476612506,1,1.02523977788995));
		rulesTest.add(new Rule(new ItemSet("\"GillAttached=free\"","\"GillSpace=close\"","\"VeilType=partial\"","\"RingNumber=one\""),new ItemSet("\"VeilColor=white\""),0.772033481043821,1,1.02523977788995));
		rulesTest.add(new Rule(new ItemSet("\"Bruises=no\"","\"GillAttached=free\"","\"VeilType=partial\"","\"RingNumber=one\""),new ItemSet("\"VeilColor=white\""),0.517971442639094,0.998102466793169,1.02329435136644));
		rulesTest.add(new Rule(new ItemSet("\"GillAttached=free\"","\"GillSpace=close\"","\"VeilType=partial\"","\"VeilColor=white\""),new ItemSet("\"RingNumber=one\""),0.772033481043821,0.950015146925174,1.03070553600696));
		rulesTest.add(new Rule(new ItemSet("\"Bruises=no\"","\"GillAttached=free\"","\"VeilType=partial\"","\"VeilColor=white\""),new ItemSet("\"RingNumber=one\""),0.517971442639094,0.928918322295806,1.00781683364465));
		rulesTest.add(new Rule(new ItemSet("\"GillAttached=free\"","\"SurfaceAboveRing=smooth\"","\"VeilType=partial\"","\"VeilColor=white\""),new ItemSet("\"RingNumber=one\""),0.559330379123584,0.911717495987159,0.989155039716837));
		rulesTest.add(new Rule(new ItemSet("\"GillAttached=free\"","\"SurfaceBelowRing=smooth\"","\"VeilType=partial\"","\"VeilColor=white\""),new ItemSet("\"RingNumber=one\""),0.529788281634663,0.907251264755481,0.98430946512734));
		rulesTest.add(new Rule(new ItemSet("\"GillAttached=free\"","\"GillSize=broad\"","\"VeilType=partial\"","\"VeilColor=white\""),new ItemSet("\"RingNumber=one\""),0.588872476612506,0.885597926693817,0.960816981364927));
		rulesTest.add(new Rule(new ItemSet("\"GillAttached=free\"","\"VeilType=partial\"","\"VeilColor=white\"","\"RingNumber=one\""),new ItemSet("\"GillSpace=close\""),0.772033481043821,0.860592755214051,1.02634403161464));
		rulesTest.add(new Rule(new ItemSet("\"GillAttached=free\"","\"SurfaceAboveRing=smooth\"","\"VeilType=partial\"","\"VeilColor=white\""),new ItemSet("\"GillSpace=close\""),0.520925652387986,0.849117174959872,1.01265823977892));
		rulesTest.add(new Rule(new ItemSet("\"GillAttached=free\"","\"GillSize=broad\"","\"VeilType=partial\"","\"VeilColor=white\""),new ItemSet("\"GillSpace=close\""),0.534958148695224,0.804516845612736,0.959467829383128));
		for(Rule rule: rules) {
			assertEquals(rule.toString(),true, rulesTest.contains(rule));
		}
			
	}
	
	
	

}
