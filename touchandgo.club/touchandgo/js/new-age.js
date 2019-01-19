(function($) {
  "use strict"; // Start of use strict
  var isTag = false;
  // Smooth scrolling using jQuery easing
  $('a.js-scroll-trigger[href*="#"]:not([href="#"])').click(function() {
    if (location.pathname.replace(/^\//, '') == this.pathname.replace(/^\//, '') && location.hostname == this.hostname) {
      var target = $(this.hash);
      target = target.length ? target : $('[name=' + this.hash.slice(1) + ']');
      if (target.length) {
        $('html, body').animate({
          scrollTop: (target.offset().top - 48)
        }, 1000, "easeInOutExpo");
        return false;
      }
    }
  });

  // Closes responsive menu when a scroll trigger link is clicked
  $('.js-scroll-trigger').click(function() {
    $('.navbar-collapse').collapse('hide');
  });

  $('#howPhone').click(function (){
    isTag = !isTag;

    if (isTag){
      $(".feature-item h3").eq(0).text("Your Role : Tagger");
      $(".feature-item p").eq(0).text("Chase hunted players to give them your role.");
      $(".feature-item circle").eq(0).css("fill", "#F43A4A");

      $(".feature-item p").eq(1).text("Mercilessly hunt one of them to give him the tagger role");

      $(".feature-item h3").eq(2).text("Objective");
      $(".feature-item p").eq(2).text("Time is working against you here. Hurry and find someone!");
      $(".feature-item img")[0].hidden = false;
      $(".feature-item svg").eq(2).hide();
     
    }
    else{
      $(".feature-item h3").eq(0).text("Your Role : Hunted");
      $(".feature-item p").eq(0).text("Identify yourself apart of other players which are also being hunted");
      $(".feature-item circle").eq(0).css("fill", "#53CC77");

      $(".feature-item p").eq(1).text("Keep track of friends which are also part of the game");

      $(".feature-item h3").eq(2).text("Tagger");
      $(".feature-item p").eq(2).text("Stay away from these people to keep your tagger timer as low as possible");
      $(".feature-item img")[0].hidden = true;
      $(".feature-item svg").eq(2).show();
    }
  })

  // Activate scrollspy to add active class to navbar items on scroll
  $('body').scrollspy({
    target: '#mainNav',
    offset: 54
  });

  // Collapse Navbar
  var navbarCollapse = function() {
    if ($("#mainNav").offset().top > 100) {
      $("#mainNav").addClass("navbar-shrink");
    } else {
      $("#mainNav").removeClass("navbar-shrink");
    }
  };
  // Collapse now if page is not at top
  navbarCollapse();
  // Collapse the navbar when page is scrolled
  $(window).scroll(navbarCollapse);

})(jQuery); // End of use strict
