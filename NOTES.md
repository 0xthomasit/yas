

* Why record class?
    _ Hold: immutable data (coordinates, API responses, configuration settings)
    _ Purpose:
        + To transfer the data without any additional behaviour:
            a. State modification
            b. Reduce boilerplate code:
                i.  Accessor methods: getter, setter
                ii. Other methods: equals(), hashCode(), toString().

* Prevent Docker containers from starting automatically on system startup:
    _ docker update --restart=no <container-name-or-id>