/**
 * @license
 * Copyright (c) 2014, 2019, Oracle and/or its affiliates.
 * The Universal Permissive License (UPL), Version 1.0
 */
/*
 * MovieList ViewModel code goes here
 */
define(['ojs/ojcore', 'knockout', 'jquery', 'text!../endpoints.json', 'ojs/ojknockout', 'ojs/ojlistview', 'ojs/ojmodel', 'ojs/ojgauge', 'ojs/ojbutton', 'ojs/ojcheckboxset', 'ojs/ojselectcombobox', 'ojs/ojpagingcontrol', 'ojs/ojcollectiontabledatasource', 'ojs/ojpagingtabledatasource', 'ojs/ojpopup'],
function(oj, ko, $, endpoints) {
    var self = this;
    self.selTitle = ko.observable('');
    self.selVoteCount = ko.observable('');
    self.selVoteAverage = ko.observable('');
    self.selPosterPath = ko.observable('');
    self.selReleaseDate = ko.observable('');
    self.selOverview = ko.observable('');

    // not implemented yet
    function MovieDetail(){
        var url = JSON.parse(endpoints).movies;
        var image = JSON.parse(endpoints).image;
        $.getJSON(url+"/"+self.movieId(), function (data) {
            $.each(data, function () {
                console.log(data.title)
                self.selTitle(data.title);
                self.selVoteCount(data.vote_count);
                self.selVoteAverage(data.vote_average);
                self.selPosterPath(image + "/" + data.poster_path);
                self.selReleaseDate(data.release_date);
                self.selOverview(data.overview);
            });
        })
    }
    function MovieListViewModel() {

        self.connected = function() {
        // Implement if needed
        };

        /**
         * Optional ViewModel method invoked after the View is disconnected from the DOM.
         */
        self.disconnected = function() {
        // Implement if needed
        };

        /**
         * Optional ViewModel method invoked after transition to the new View is complete.
         * That includes any possible animation between the old and the new View.
         */
        self.transitionCompleted = function() {
        // Implement if needed
        };

        self.movieId = ko.observable();

        self.openListener = function (event)
        {
            movieId(event.target.id);
            var sel = event.target.firstSelectedItem;
            // not implemented

            var popup = document.getElementById('popup1');
            popup.open('');
        };

        this.startAnimationListener = function(event)
        {
            var ui = event.detail;
            if (event.target.id !== "popup1")
                return;
                
            if ("open" === ui.action)
            {
                event.preventDefault();
                var options = {"direction": "top"};
                oj.AnimationUtils.slideIn(ui.element, options).then(ui.endCallback);
                
                var vm = new MovieDetail();
                ko.cleanNode(document.getElementById('selMovieContent'));
                ko.applyBindings(vm, document.getElementById('selMovieContent'));
            }
            else if ("close" === ui.action)
            {
                event.preventDefault();
                ui.endCallback();
            }
        }.bind(this);
    }

    this.imagePath = JSON.parse(endpoints).image;

    var model = oj.Model.extend({
        idAttribute: 'ID'
    });

    this.collection = new oj.Collection(null, {
        url: JSON.parse(endpoints).movies,
        model: model
    });
    var originalCollection = this.collection;

    this.dataSource = ko.observable(new oj.PagingTableDataSource(new oj.CollectionTableDataSource(this.collection)));

    this.currentPrice = [];
    this.currentAuthor = [];
    this.currentRating = [];
    this.currentSort = ko.observable("default");

    this.handleSortCriteriaChanged = function(event, ui)
    {
        var criteria = criteriaMap[event.detail.value];
        self.dataSource().sort(criteria);
    };

    this.handleFilterChanged = function(event, ui)
    {
        var value = event.detail.value;
        if (!Array.isArray(value))
        {
            return;
        }

        self.collection = originalCollection;
        self.dataSource(new oj.PagingTableDataSource(new oj.CollectionTableDataSource(self.collection)));
    };

    return new MovieListViewModel();
}
);
