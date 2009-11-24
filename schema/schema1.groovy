import automenta.netention.*;
import automenta.netention.server.*;
import automenta.netention.server.impl.*;
import automenta.netention.client.property.*;
import automenta.netention.client.value.*;

import automenta.netention.client.value.real.*;
import automenta.netention.client.value.geo.*;
import automenta.netention.client.value.node.*;
import automenta.netention.client.value.string.*;
import automenta.netention.client.value.integer.*;

/*
schema.newPattern("testReal", "_Real Number Test").
		withReal("rn1", "rn1").
		withReal("rn2", "rn2").
		withReal("rn3", "rn3").
		withReal("rn4", "rn4");

schema.newPattern("testString", "_String Test").
	withReal("s1", "s1").
	withReal("s2", "s2").
	withReal("s3", "s3").
	withReal("s4", "s4");

schema.newPattern("testInteger", "_Integer Test").
	withInt("i1", "i1").
	withInt("i2", "i2").
	withInt("i3", "i3").
	withInt("i4", "i4");

schema.newPattern("testNode", "_Node Test").
	withNode("n1", "n1").
	withNode("n2", "n2").
	withNode("n3", "n3").
	withNode("n4", "n4");

schema.newPattern("PhysicalObject", "Object").
	   description("A physical, tangible object").
	   	withReal("mass", "Mass", Unit.Mass).
	   	withReal("length", "Length", Unit.Distance).
	   	withReal("width", "Width", Unit.Distance).
	   	withReal("height", "Height", Unit.Distance);

			
schema.newPattern("owned", "Owned").
	   description("Something that is or can be owned").
			withNode("owner", "Owner", "agent").
			withNode("ownerNext", "Next Owner", "agent");

schema.newPattern("Rented", "Rented").
	   description("Something that is or can be rented/loaned/leased").
			withNode("renter", "Renter", "agent").
			withNode("renterNext", "Next Renter", "agent").
			withReal("dailyCost", "Daily Cost", Unit.Currency);
			
schema.newPattern("located", "Located").
  	   description("Something that has a specific location").
			withGeoPoint("location", "Location").
			withGeoPoint("locationNext", "Next Location");

schema.newPattern("event", "Event").
		extending('located').
			withTimePoint('startTime', 'Start Time').
			withTimePoint('endTime', 'End Time');

schema.newPattern("human", "Human").
		extending('located').
		description("Human being").
			withString("firstName", "First Name").
			withString("lastName", "Last Name").
			withRichString("biography", "Biography");
		

schema.newPattern("agent", "Agent").
	   extending("located");
	
schema.newPattern("built", "Built").
	   extending("PhysicalObject").
	   description("Something that is built or manufactured").
	   		withString("builder", "Builder Name").
	   		withString("serialID", "Serial Number").
	   		withTimePoint("builtWhen", "When Built");

schema.newPattern("bicycle", "Bicycle").
       extending("built", "owned", "located").
       		withInt("gearCount", "Gear Count").
       		withReal("wheelDiameter", "Wheel Diameter", Unit.Distance).
       		withString("bicycleType", "Bicycle Type", [ 'mountain',
       			"street",
       			"hybrid",
       			"tricycle",
       			"unicycle",
       			"tandem",
       			"recumbent"
       		]);


schema.newPattern("Dwelling", "Dwelling").
		extending("built", "located").
			withInt("numBedrooms", "Bedrooms");


schema.newPattern("guitar", "Guitar");
*/


Agent ann = network.newAgent("ann", "Ann"); 
	ann.newDetail('Myself', 'Human').
		with('firstName', new StringIs("Ann")).
		with('lastName', new StringIs("Onymous")).
		with('biography', new StringIs("<h1>Hi!</h1><p>This is my <b>biography</b>!</p>"));

	ann.newDetail('My Bike', 'Bicycle').
		with('gearCount', new IntegerIs(6)).
		with('wheelDiameter', new RealIs(0.25)).
		with('owner', new NodeIs('ann')).
		with('ownerNext', new NodeNotEquals("ann")).
		with('bicycleType', new StringIs("mountain"));
	
	ann.newDetail("My Apartment", 'Dwelling', 'Rented');

Agent bob  = network.newAgent("bob", "Bob"); 
	bob.newDetail('Bike I Want', 'Bicycle'). 
		with('gearCount', new RealMoreThan(4)).
		with('ownerNext', new NodeIs('bob')).
		with('owner', new NodeNotEquals('bob'));	//not really necessary

Agent carl  = network.newAgent("carl", "Carl");
	carl.newDetail('My Next Bike', 'Bicycle'). 
		with('gearCount', new RealMoreThan(2)).
		with('ownerNext', new NodeIs('carl')).
		with('owner', new NodeNotEquals('carl'));	//not really necessary
		
Agent tester = network.newAgent("tester", "Tester");
	tester.newDetail("Real Test", 'testReal').
		with('rn1', new RealIs(3.14159)).
		with('rn2', new RealEquals(3.14159)).
		with('rn3', new RealMoreThan(3)).
		with('rn4', new RealLessThan(4));
	
	tester.newDetail("String Test", 'testString').
		with('s1', new StringIs("xyz")).
		with('s2', new StringEquals("XYZ")).
		with('s3', new StringContains("x")).
		with('s4', new StringNotContains("abc"));
	
	tester.newDetail("Integer Test", 'testInteger').
		with('i1', new IntegerIs(1)).
		with('i2', new IntegerEquals(2));
		//with('i3', new IntegerNotEquals(3)).
		//with('i4', new IntegerMoreThan(4)).
		//with('i5', new IntegerLessThan(4)).
		//with('i6', new IntegerBetween(5,7)).
	
	tester.newDetail("Node Test", 'testNode').
		with('n1', new NodeIs("tester")).
		with('n2', new NodeEquals("tester")).
		with('n3', new NodeNotEquals("tester"));

		
		
		
//		<pattern id="guitar" name="Guitar">
//			<extends>built</extends>
//			<extends>owned</extends>
//			<extends>located</extends>
//			<variables>
//				<integer id="strings" name="Strings Count">
//					<value>6</value>
//					<value>12</value>
//				</integer>
//				<string id="guitarType" name="Guitar Type">
//					<value>electric</value>
//					<value>acoustic</value>
//					<value>midi</value>
//				</string>
//				<string id="pickupType" name="Pickup Type">
//					<value>stratocaster</value>
//					<value>humbucker</value>
//					<value>optical</value>
//				</string>
//			</variables>
//		</pattern>
