# Getting started #
## Before to start ##
You must know:

| **The concept of Deferred Binding**: | <a href='http://code.google.com/webtoolkit/doc/latest/DevGuideCodingBasicsDeferred.html'>Dev Guide of GWT (<b>Deferred Binging</b>)</a>|
|:-------------------------------------|:---------------------------------------------------------------------------------------------------------------------------------------|

> | With this technique the GenValGui framework creates in compilation time the implementation of the GUIs of the POJOs. In order to support the users validation needs, the generated code includes the implementation of Validation instructions (groups, built-in constraints, custom error messages, and custom  constraints)|
|:-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|

| **Knowledge of the Validation API based on standard JSR-303** | <a href='http://download.oracle.com/otndocs/jcp/bean_validation-1.0-fr-oth-JSpec/'>The spec</a> | <a href='http://code.google.com/p/google-web-toolkit/wiki/BeanValidation'>The implementation of that spec in the client side of GWT</a> |
|:--------------------------------------------------------------|:------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|

| **The principal purpose of this framework** | [GenValGui](GenValGui.md) |
|:--------------------------------------------|:--------------------------|

> |The GenValGui framework allows automatic generating of GUIs from the data model of your application. These GUI´s supports automatic data-binding in the client side (if you made any change in the information it saves in the Structure Data of the App, you must be careful because there isn’t support to transport the changes to the DB yet), and an intensive use of data, because the generated code uses CellWidgets|
|:------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|

## The basic operation of that framework ##
  1. Create your POJO and introduce in them the Constraints that you need.
  1. Mark in the GenValGui framework a reference to the POJOs created in the previous step.
  1. Declare an instance of the GenValGui framework
  1. Use the instance of the step 3 to represent the different GUIs of the classes created



---


## How to use step by step ##

> |  **1** |<a href='http://code.google.com/webtoolkit/download.html'>download</a> the lest version of the GWT SDK |
|:-------|:------------------------------------------------------------------------------------------------------|

> | **2** | <a href='http://code.google.com/p/genvalgui/downloads/list'>download</a> the last version of **GenValGui.jar** from  and include it into your project|
|:------|:-----------------------------------------------------------------------------------------------------------------------------------------------------|

> | **3** |Create the POJO classes that you need (_in the example I put the files into the **com.example.myproject.shared**_) |
|:------|:------------------------------------------------------------------------------------------------------------------|

> | **4** | Create the next structure (_you can download the example project_) |
|:------|:-------------------------------------------------------------------|
> > | <img src='http://genvalgui.googlecode.com/svn/resources/imgs/estructura.png' alt='Step 1 - Architecture' height='470' width='370' />  |


> | **com.example.myproject.genvalgui** |
|:------------------------------------|

> In this package you must include the classes that bring the entry point information to the framework.

> As you can see, you have to indicate the groups and the classes that will be validate (into the **@GwtValidation** annotation), the classes that will be automatically generated (into the  **@GvgInfoAnnotation**) and the method createBuilder.

> All this content must be in a class called **GenValGuiFactory** (you can copy/paste and change only add your information). When finished, the GWT compiler via deferred binding allows you to use the magic method:

```
    public T CellTable  buildGui(Class c, Object o);
```
> It allows create automatically the UI’s and have an instant access to the different user interfaces.

> | **com.example.myproject.genvalgui.validator** |
|:----------------------------------------------|

> In this package and in the packages that belongs to it you must put all the classes that configure the part of validation.

> In concrete, in this package you must put the entry point to Validation API **GwtValidatorFactory.java**.

> | **com.example.myproject.genvalgui.validator.constraints** |
|:----------------------------------------------------------|

> In this package you must put the definition of your own constraints (it could be that you don’t need it, because the JSR includes the most common built-in constraints, you can <a href='http://docs.jboss.org/hibernate/validator/4.1/reference/en-US/html/validator-usingvalidator.html#validator-defineconstraints-builtin'>visit it </a>.


> In the example project you can find the constraint @Dni (Dni.java). That constraint represents a number that identifies a person (the number is generated by an algorithm, and it is easy check if the number is correct) will check if the id of a person is correct.

> | **com.example.myproject.genvalgui.validator.constraints.impl** |
|:---------------------------------------------------------------|

> In this package you must put the class that check if the number is correct (the standard recommend use the same class of the constraint finished by Validator).

> | **com.example.myproject.genvalgui.message** |
|:--------------------------------------------|

> Finally in that package you have to put the configuration files of your custom error messages  (when an error is raised, you can show your custom error message)

> I suggest read the spec because you hace three ways to introduce your customs messages:
    * When you use the constraint in the POJO class: **@Dni(message="Dummy message error")**
    * In the attribute message in the constraint **Dni.java**:**String message() default{"Dummy message error"}**
    * In the structure files described above

> |  **5** | The fifth step is include in the configuration file (_xxx.gwt.xml_)of your project the references to that classes. |
|:-------|:-------------------------------------------------------------------------------------------------------------------|
```
   <replace-with
    class="com.example.myproject.genvalgui.validator.GwtValidatorFactory">
    <when-type-is class="javax.validation.ValidatorFactory" />
  </replace-with>
 
  <!--  specify the ValidationMessageResolver to use for your custom validation messages -->
  <replace-with
    class="com.example.myproject.genvalgui.validator.message.CustomValidationMessagesResolver">
    <when-type-is
      class="com.google.gwt.validation.client.UserValidationMessagesResolver" />
  </replace-with>
  
  <generate-with class="es.codegraff.gwt.genvalgui.rebind.GvgBuilderGenerator">
    <when-type-assignable class="es.codegraff.gwt.genvalgui.client.GvgInterfaceBuilder" />
  </generate-with>
 
  <!-- Specify the app entry point class.                         -->
  <entry-point class='com.example.myproject.client.ExampleGenValGui'/>

  <!-- Specify the paths for translatable code                    -->
  <source path='client'/>
  <source path='shared'/>
  <source path='genvalgui'/>
```

> |  **6** | Use the framework |
|:-------|:------------------|
```
public void onModuleLoad() {
     // You can avoid this lines and call to your DB in order to get de objects
       ArrayList<Person> alPerson = new ArrayList<Person>();
       Person p = fillPerson();
       Person p2 = fillPerson();
       Person p3 = null;
       alPerson.add(p);alPerson.add(p2);alPerson.add(p3);
     //----------------------------------------------------------
		
     GwtGenValGuiBuilder builder =(GwtGenValGuiBuilder) GenValGuiFactory.createBuilder();
				
     CellTable<Person> aux = (CellTable<Person>) builder.buildGui(Person.class, alPerson);
     
     RootPanel.get().add(aux);
}
```