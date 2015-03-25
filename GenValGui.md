## GenValGui ##
| **GenValGui:** | **Generator** **Validation** **GUIs** | **3 steps** |
|:---------------|:--------------------------------------|:------------|

Basically, we can separate into three different steps the internal behaviour of that framework.
> | The first step:**"Introducing info into the Genvalgui Framework"**|
|:---------------|
In the first step the user have to indicate (using annotations):
    1. The classes that will be validated into the Validation framework
    1. The POJO classes that will be available to show into the browser.
    1. The custom configuration.

To implement that, we use three annotations (_keep in mind, be patient. We’ll see their use in an example later_):

> - **@GwtValidation:** Represents the entry point to the validation API, you must include the type of classes that will be validated.

> - **@GvgInfoAnnotation:** Like the previous annotation, you must include here the classes that will be parsed to generate their GUIs.

```
   package com.example.myproject.genvalgui;

   public final class GenValGuiFactory {

        @GwtValidation(value = {Person.class, Adress.class, Email.class, ArrayList.class, HashMap.class}
                      ,groups = {Default.class})
	public interface GwtValidator extends Validator {
	}
	
	@GvgInfoAnnotation(value={Person.class,Adress.class,Email.class})
	public interface GwtGenValGuiBuilder extends GvgInterfaceBuilder{

	}
	public static GwtGenValGuiBuilder createBuilder() {
		return GWT.create(GwtGenValGuiBuilder.class);
	}
}
```

| | | | <img src='http://genvalgui.googlecode.com/svn/resources/imgs/Architecture-Step1.jpg' alt='Step 1 - Architecture' height='250' width='520' /> |
|:|:|:|:---------------------------------------------------------------------------------------------------------------------------------------------|

> - **@Caption:** This annotation is used in the POJO classes to indicate the name of the columns. You can customize the appearance of the GUIs.
```
    //Example
    @Caption(name="Info", button="Mails")
    private HashMap<String, Email> info;
```


Those annotations are placed in the principal class (entry point) of this framework (_keep in mind, it’ll useful to understand completely the example_).


---

> | The second:**"Generate the code in compilation time"**|
|:-----------|
The second step is to generate the code of that GUIs, using **Deferred Binding** (to generate the code in compilation time), **CellWidget** (the classes that will represent the attributes and **client databinding**) and the **JSR-303** (to implement all the validation behaviour).

Basically, this part is divided in two process. The first process is the attendant to parse all the POJO classes (in our example Person.class, Adress.class ...), and save the information (constraints, types of attributes, ...) into an internal tree.

Later, the second process consists in generate the different GUIs from the previously generate tree ( the algorithm "paint" a different CellWidget for each attribute of each class, and attach it the necessary validation code)

_For more information about this part visit: **InternalArchitecture**_


---

> | The third step: **Use the generated GUIs** |
|:-------------------------------------------|

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

---


A good summary of that steps are:

| <img src='http://genvalgui.googlecode.com/svn/resources/imgs/StepsSummary.png' alt='Step 1 - Architecture' height='415' width='850' />  |
|:----------------------------------------------------------------------------------------------------------------------------------------|