# Internal Architecture #

The internal process to generate the GUIs, are composed by two differentiated steps.

## Step 1: Save the info into a tree ##

The first step is the attendant to parse all de POJO classes that the developer was introduced (in our example Person.class, Email.class, Adress.class ...), in order to save the information that we'll need in the next step.

| **Why are we interested in save the information and not passed directly?** |
|:---------------------------------------------------------------------------|

Because one of the principal purpose of this framework is the **scalability** and maintenance.

If in the future we want to introduce new information to save (for example a new annotation that allows attach a css file) we can do it in an easy way, we don't need only a few changes.

| **Which is the classes that are involved??** |  Save the information into a tree |
|:---------------------------------------------|:----------------------------------|

  * **GvgGeneratorNode**
That class represents the node of the tree. It saves one sub-tree for each POJO class that the user has introduced previously.

  * **GvgGeneratorNodeClass**

That class saves the name of the CellTable that will be associated into the next step.
Also saves an ArrayList of GvgGeneratorNodeField. It represents the information of each attribute that the POJO class has.

  * **GvgGeneratorNodeField**

In order to save all the information of each attribute we use this class (constraints, a reference to the parent and child nodes (for example if you have an attribute like: ArrayList< String>, we'll have two nodes, one represents the ArrayList and other represents the String type.

## Step 2: Generate the GUIs ##

One we have the information the next step is to create the differents GUIs in order to be able the use into the app.

| **How do we this...** |
|:----------------------|

The idea is simple, we have one or more classes that has attributes (al of them are representated into the classes that we've seen in the previous step).

So, is mandatory has a good generator code structure listening the different types of the attributes.

For example, if one class has a String, Integer, ArrayList< String> attributes, respectively, we expect that there are one Constructor of each type.

The problem is,

| **How do we this...**| **efficiently?**| **scalable?** |
|:---------------------|:----------------|:--------------|

With the implementing of a **Chain of Responsibility Pattern**.

I've created a class (**CellConstructor**), that implements that pattern. This class, are waiting for petitions to generate code. When it receives once, this class send to the chain.

For example if the class receives a **String type** and the **order of the chain** are **JavaUtilCellConstructor |  PrimitiviveCellConstructor | StringCellConstructor**, the class JavaUtilCellConstructor will send the petition to the next participant, and the class PrimitiveCellConstructor will do the same, because the types that are representing are different.

Finally, the class StringCellConstructor will generate the code.

In order to do **scalable**, imagine for a moment, if we want include **support for different types** (for example the **Date.class**). We must create only a class (DateCellConstructor) that extends from CellConstructor and include it into the chain **(nothing else. Really good, isn't it?)**


---

## Diagram that represents the classes that are involved ##

> <img src='http://genvalgui.googlecode.com/svn/resources/imgs/InternalArchitecture.png' alt='Step 1 - Architecture' height='415' width='850' />