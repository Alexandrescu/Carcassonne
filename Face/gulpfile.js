'use strict';

var _ = require('lodash'),
    gulp = require('gulp'),
    gulpLoadPlugins = require('gulp-load-plugins'),
    runSequence = require('run-sequence'),
    defaultAssets = require('./config/assets'),
    plugins = gulpLoadPlugins();

gulp.task('env:dev', function(){
  process.env.NODE_ENV = 'development';
});

gulp.task('env:prod', function () {
  process.env.NODE_ENV = 'production';
});

// Lint CSS and JavaScript files.
gulp.task('build', function(done) {
  //runSequence('sass', ['csslint', 'jshint'], done);
  runSequence(['sass', 'js', 'dependencies'], done);
});

gulp.task('dependencies', function() {
  gulp.src(defaultAssets.dependencies)
    .pipe(gulp.dest(defaultAssets.client.js));
});

gulp.task('js:app', function() {
  gulp.src(defaultAssets.server.appLocation + 'app.js')
    .pipe(gulp.dest(defaultAssets.client.app));
});

gulp.task('js:controllers', function() {
  gulp.src(defaultAssets.server.appLocation + 'controllers/**')
    .pipe(plugins.concat('controllers.js'))
    .pipe(gulp.dest(defaultAssets.client.app));
});

gulp.task('js:directives', function() {
  gulp.src(defaultAssets.server.appLocation + 'directives/**')
    .pipe(plugins.concat('directives.js'))
    .pipe(gulp.dest(defaultAssets.client.app));
});

gulp.task('js:services', function() {
  gulp.src(defaultAssets.server.appLocation + 'services/**')
    .pipe(plugins.concat('services.js'))
    .pipe(gulp.dest(defaultAssets.client.app));
});

gulp.task('js', function(done) {
  runSequence(['js:app', 'js:controllers', 'js:directives', 'js:services'], done);

});

gulp.task('sass', function () {
  return gulp.src(defaultAssets.server.sass)
    .pipe(plugins.sass())
    .pipe(gulp.dest(defaultAssets.client.css));
});

gulp.task('nodemon', function () {
  return plugins.nodemon({
    script: 'server.js',
    nodeArgs: ['--debug']
  });
});

// Watch Files For Changes
gulp.task('watch', function() {
  // Start livereload
  plugins.livereload.listen();
  gulp.watch(defaultAssets.client.views).on('change', plugins.livereload.changed);
  gulp.watch(defaultAssets.server.app, ['js']).on('change', plugins.livereload.changed);
  gulp.watch(defaultAssets.server.routes).on('change', plugins.livereload.changed);
  gulp.watch( _.union(defaultAssets.server.views, defaultAssets.server.allJS, defaultAssets.server.config, defaultAssets.server.server))
    .on('change', plugins.livereload.changed);
});

gulp.task('default', function(done) {
  runSequence('env:dev', 'build', ['nodemon', 'watch'], done);
});
