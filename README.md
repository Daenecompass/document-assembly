**Rule-Based Dialog-Driven Legal Document Assembly**

Author: Marko MarkoviÄ‡

The aim of this project is to demonstrate a legal document assembly method that uses legal rules and document templates for interactive fact collection.
It is developed to work out the ideas that are the starting point of author's PhD research at Faculty of Technical Sciences, University of Novi Sad.

Legal rules and document templates were modeled on The Criminal Procedure Code, The Criminal Code and The Law on Road Traffic Safety (which prescribe road traffic related criminal offences in Republic of Serbia) and indictments / motions to indict selected from examplary cases.
The software is written in Java programming language and uses [tuProlog](http://apice.unibo.it/xwiki/bin/view/Tuprolog/) library.

The source code is organized in an Eclipse project with the following structure:<br/>
/<br/>
&nbsp;&nbsp;&nbsp;&nbsp;motion_to_indict.xml - document template<br/>
&nbsp;&nbsp;&nbsp;&nbsp;readme.txt - this file<br/>
&nbsp;&nbsp;&nbsp;&nbsp;traffic_accident.pl - legal rules (in Prolog)<br/>
/src/org/draftingbyrules<br/>
&nbsp;&nbsp;&nbsp;&nbsp;Main.java - contains the main method that initiates document assembling process<br/>
/src/org/draftingbyrules/bean<br/>
&nbsp;&nbsp;&nbsp;&nbsp;ArgumentNode.java - a node in an argument graph<br/>
&nbsp;&nbsp;&nbsp;&nbsp;Fact.java - a variable name/value pair used to represent a fact in legal rules or document templates<br/>
/src/org/draftingbyrules/utils<br/>
&nbsp;&nbsp;&nbsp;&nbsp;AkomaNtosoUtil.java - contains methods that generate indictments / motions to indict in [Akoma Ntoso](http://www.akomantoso.org/) from an existing document template<br/>
&nbsp;&nbsp;&nbsp;&nbsp;CafUtil.java - contains methods that generate argument graphs in CAF ([Carneades](http://carneades.github.io/) argument format)<br/>
&nbsp;&nbsp;&nbsp;&nbsp;UiUtil.java - command-line user interface <br/>
/lib<br/>
&nbsp;&nbsp;&nbsp;&nbsp;tuprolog.jar - tuProlog library<br/>


Default configuration parameters are stored in Main.java. When started, the program uses command-line interface to gather facts relevant to the case. Prolog rules are used to construct relevant questions by searching predicates that have to be satisfied in order for the indictment to be true. The document template is also used to collect facts. After the user answers all the questions, the argument graph and the indictment / motion to indict are generated.

Two types of questions are being asked: questions regarding the fulfillment of predicates and questions regarding the values of variables. The first type of questions are yes/no questions that are answered with 'y' or 'n' letters (assuming 'y' as the default value). The second type of questions also has default values - the first value of the variable with the same name already answered by the user.

For example, one violation of traffic rules (not keeping safe distance from other vehicles) could be determined from the answer to the following question:
```
    unsafe_distance(Defendant) [y/n]: 
```
After answering the question with 'y', 'n' or only <enter>, the variable value should be entered:
```
    Defendant: 
```
If, for instance, the value 'john' is entered, the existence of defendant's negligence would be determined by:
```
    negligence(Defendant) [y/n]: 
```
and the default value for the variable is offered:
```
    Defendant [john]: 
```
