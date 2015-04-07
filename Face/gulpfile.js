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
gulp.task('lint', function(done) {
  //runSequence('sass', ['csslint', 'jshint'], done);
  runSequence('sass', done);
});

// CSS linting task
gulp.task('csslint', function (done) {
  return gulp.src(defaultAssets.client.css)
    .pipe(plugins.csslint('.csslintrc'))
    .pipe(plugins.csslint.reporter())
    .pipe(plugins.csslint.reporter(function (file) {
      if (!file.csslint.errorCount) {
        done();
      }
    }));
});

// JS linting task
gulp.task('jshint', function () {
  return gulp.src(_.union(defaultAssets.server.allJS, defaultAssets.client.js))
    .pipe(plugins.jshint())
    .pipe(plugins.jshint.reporter('default'))
    .pipe(plugins.jshint.reporter('fail'));
});

gulp.task('sass', function () {
  return gulp.src(defaultAssets.client.sass)
    .pipe(plugins.sass())
    .pipe(gulp.dest(defaultAssets.client.css));
});

gulp.task('nodemon', function () {
  return plugins.nodemon({
    script: 'server.js',
    nodeArgs: ['--debug'],
    ext: 'js,html',
    watch: _.union(defaultAssets.server.views, defaultAssets.server.allJS, defaultAssets.server.config, defaultAssets.server.server)
  });
});

// Watch Files For Changes
gulp.task('watch', function() {
  // Start livereload
  plugins.livereload.listen();
  gulp.watch(defaultAssets.client.views).on('change', plugins.livereload.changed);
});

gulp.task('default', function(done) {
  runSequence('env:dev', 'lint', ['nodemon', 'watch'], done);
});
