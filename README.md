# What is MoSKito-Central?

**MoSKito-Central is the data warehouse for [MoSKito Monitoring system](http://moskito.org).**

It stores the performance data, collected by [MoSKito-Core](https://github.com/anotheria/moskito/tree/master/moskito-core).

## Why do I need it?

By design, MoSKito-Core does not actually store data. It keeps nothing but the values, obtained within the last monitoring interval. 

To store data (in application memory, filesystem or database) for later processing, you will need MoSKito-Central.

## How does it work?

<img src="https://confluence.opensource.anotheria.net/download/attachments/25100442/moskito-central_connectors.png" width="500">

Read more about [the way it works](https://confluence.opensource.anotheria.net/display/MSK/MoSKito-Central+Overview#MoSKito-CentralOverview-howitworks) or [communication between MoSKito-Core and MoSKito-Central...](https://confluence.opensource.anotheria.net/display/MSK/Core+-+Central+Communication)

# Why is it good?

**Centralised data warehouse**: stores the entire performance info of your application, as long as you wish.

**Traces data flow between application nodes**, monitoring every system call.

**Data privacy**: keeps collected data locally, with no 3rd party resources involved.

**Multiple data transfer protocols**: RMI and HTTP come 'out-of-the-box', connectors for other protocols are easily configured.

**Flexibility in data storage**: application memory, filesystem (JSON, XML, CSV or any other file format) or database (SQL or non-SQL).

**Embedded or standalone modes**, depending on the size and complexity of your app.

[Read more about these features...](https://confluence.opensource.anotheria.net/display/MSK/MoSKito-Central+Overview)

#Install & Config
1. **Set it up ([remote (standalone) mode](https://confluence.opensource.anotheria.net/display/MSK/Setting+Up+MoSKito-Central+in+Remote+Mode) OR [embedded mode](https://confluence.opensource.anotheria.net/display/MSK/Set+Up+Moskito-Central+in+Embedded+Mode))**
2. **Set up [connectors](https://confluence.opensource.anotheria.net/display/MSK/Setting+up+and+Starting+REST+and+DistributeMe+%28DiMe%29+Connectors)**
3. **Configure [storage](https://confluence.opensource.anotheria.net/display/MSK/Configure+MoSKito-Central+Storage)**

#License

MoSKito-Central, as well as other MoSKito Projects, is free and open source (MIT License). Use it as you wish.

# Project resources

#### [Webpage](http://www.moskito.org/moskito-central.html)
#### [Documentation](https://confluence.opensource.anotheria.net/display/MSK/MoSKito-Central)
#### [FAQ](https://confluence.opensource.anotheria.net/display/MSK/MoSKito+FAQ)

# Support and Feedback

**We're willing to help everyone.**

For any questions, write to [moskito@anotheria.net](mailto: moskito@anotheria.net).