// SUB MENU LISTING
// Column 1: URL
// Column 2: Menu Label
// Column 3: either '' or definition/explanation of acronym/thing

var quickLinks = [
	['/review2016','2016 Laboratory Review',''],
	['/res/HABs_and_Hypoxia','Algal Blooms and Hypoxia',''],
	['/res/glcfs/','Coastal Forecasting (GLCFS)','Great Lakes Coastal Forecasting System'],
	['http://coastwatch.glerl.noaa.gov/','CoastWatch',''],
	['http://ciglr.seas.umich.edu/','Cooperative Institute (CIGLR)','Cooperative Institute for Great Lakes Research (External Link)'],
	['/data/dashboard/','Great Lakes Dashboard',''],
    ['/data/ice/','Ice Cover',''],
	['/glansis','Invasive Species (GLANSIS)','Great Lakes Aquatic Nonindigenous Species Information System'],
	['/pubs/','Publications',''],
	['http://www.regions.noaa.gov/great-lakes/index.php/regional-team/','Regional Collaboration Team','Great Lakes Regional Collaboration Team'],
	['/res/straitsOfMackinac/','Straits of Mackinac',''],
    ['/data/wlevels/','Water Levels',''],
    ['/metdata/','Met Stations, Buoys, &amp; Webcams','Real-Time Meteorological Observation Network']
	//['/lmfs/','Lake Michigan Field Station','Lake Michigan Field Station'],
	//['/res/projects/food_web/food_web.html','Food Web Diagrams',''],
	//['http://www.glerl.noaa.gov/res/glcfs/currents/','Surface Currents',''],   
    //['/facil/triptik.html','Visitors Info','']
];

var aboutLinks = [
	['/about/collaborating.html','Collaborating with Us',''],
	['/home/contactus.html','Contact Info',''],
    ['/about/history.html','History',''],
	['/about/jobs.html','Job Opportunities',''],
	['/lmfs/index.html','Lake Michigan Field Station','Lake Michigan Field Station'],
	['/library/', 'Library'],
	['/about/organization.html','Organization',''],
	['/about/partnerships.html','Partnerships',''],
	['/res/Profiles/', 'Researchers', ''],
    ['/about/strategic_plan.html','Strategic Plan',''],
    ['/about/visitors.html','Visitor Info','']    
];

var resLinks = [
    ['/res/Programs/eco_dyn/eco_dyn.html','Ecosystem Dynamics',''],
    ['/res/Programs/ipemf/ipemf.html','IPEMF (Modeling)','Integrated Physical and Ecological Modeling and Forecasting'],
    ['/res/Programs/osat/osat.html','OSAT (Observations)','Observing Systems and Advanced Technology'],
	['/res/Profiles/', 'Researchers', ''],
	['/res/currentprojects.html','Current Projects',''],
];

var pubLinks = [
    ['/pubs/index.html#peer','Peer Reviewed',''],
    ['/pubs/index.html#techRep','Technical Reports',''],
    ['/pubs/index.html#posters','Scientific Posters',''],
];

var edLinks = [
	['https://noaaglerl.wordpress.com/','Blog',''],
	['/pubs/brochures/','Fact Sheets',''],
	['/pubs/brochures/infographics.html','Infographics',''],
	['/library/','Library',''],
	['/education/newsletter.html','Newsletters',''],
	['https://www.flickr.com/photos/noaa_glerl/','Photos',''],

];

var submenuGen = function(menuDat){
    var newSubmenu = document.createElement('div');
    newSubmenu.className = 'headerNavSubMenu';
    
    for(var i = 0; i < menuDat.length; i++){
        var newMenuItem = document.createElement('a');
        newMenuItem.className = 'headerNavSubMenuItem headerNavBarLink';
        newMenuItem.setAttribute('href', menuDat[i][0]);
        newMenuItem.setAttribute('title', menuDat[i][2]);
        newMenuItem.innerHTML = menuDat[i][1];
        newSubmenu.appendChild(newMenuItem);
        newSubmenu.appendChild(document.createElement('br'));
    }
    
    return(newSubmenu);
};

var generateSubmenu = function(e){
   if($('.headerNavSubMenu').length > 0){
        $('.headerNavSubMenu').off('mouseenter mouseleave');
        $('.headerNavSubMenu').fadeOut(200);
        $('.headerNavSubMenu').remove();
   }
   var currSubmenu = null;
   
   var hoverItem = $(this).html();
   switch($(this).html()){
        case 'Quick Links':
            currSubmenu = $(submenuGen(quickLinks));
            break;
        case 'About Us':
            currSubmenu = $(submenuGen(aboutLinks));
            break;
        case 'Research':
            currSubmenu = $(submenuGen(resLinks));
            break;
        case 'Publications':
			currSubmenu = $(submenuGen(pubLinks));
            break;
		case 'Education':
			currSubmenu = $(submenuGen(edLinks));
            break;
        default:
            break;
   }
   
   $(currSubmenu).fadeIn(200);
   $('#headerNavBar').children('nav').append(currSubmenu);
   $(currSubmenu).css('left', $(this).position().left-3); 
   $(currSubmenu).hover(function(e){
   }, 
   function(e){
       $(this).fadeOut(200);
   });
   
   
};

function init(){    
    if($('.headerNavSubMenu').length > 0){
        $('.headerNavSubMenu').fadeOut(200);
    }
    
    $('.headerNavButton').hover(generateSubmenu, 
    function(e){        
    });
    
    $(window).click(function(e){
        if($('.headerNavSubMenu').length > 0){
            $('.headerNavSubMenu').fadeOut(200);
        }
    });
	
	//For announcements
	//FFAA21 - Alerts
	
	var currTime = new Date().getTime();
	var expireTime = new Date(2016, 9, 18, 12, 30).getTime();
	
	if(currTime <= expireTime){
	
		$('#mainContentInner').prepend('<div id="webAnnounce" style="border: 2px dotted #FFAA21; padding: 10px; width: 90%; margin: 10px auto;"><strong>Notice:</strong> GLERL will be performing server maintenance that will render the website and lab phone system unavailable for the morning of October 18, 2016 and should be completed by noon. If you need assistance, please contact us at <a href="mailto:www.glerl@noaa.gov">www.glerl@noaa.gov</a></div>');
		
		window.setTimeout(function(e){
			$('#webAnnounce').fadeOut(500);
		}, 
		20000);
	
	}
}

//For the front page slideshow
function insertCoin(){
    $('#coin-slider').coinslider({
        width: 950, // width of slider panel
        height: 300, // height of slider panel
        spw: 1, // squares per width
        sph: 1, // squares per height
        delay: 6000, // delay between images in ms
        sDelay: 30, // delay beetwen squares in ms
        opacity: 0.7, // opacity of title and navigation
        titleSpeed: 500, // speed of title appereance in ms
        effect: 'rain', // random, swirl, rain, straight
        navigation: true, // prev next and buttons
        links : true, // show images as links
        hoverPause: true // pause on hover
    });
	
	$('#coin-prev').fadeOut(100);
	$('#coin-slider').fadeIn(100);
	
}

function insertFlex(){
   $('#topThumbs').flexslider({
        animation: "slide",
        controlNav: false,
        slideshowSpeed: 10000,
        itemWidth: 141,
        itemMargin: 5,
        asNavFor: '#topGallery',
        move: 4
    });

    $('#topGallery').flexslider({
        animation: "fade",
        controlNav: false,
        animationLoop: true,
        slideshowSpeed: 6000
        //sync: "#topThumbs"
    }); 
}
