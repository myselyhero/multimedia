package com.yongyongwang.multimedia.choose.magic.filter.helper;


import com.yongyongwang.multimedia.choose.magic.filter.advanced.MagicAmaroFilter;
import com.yongyongwang.multimedia.choose.magic.filter.advanced.MagicAntiqueFilter;
import com.yongyongwang.multimedia.choose.magic.filter.advanced.MagicBlackCatFilter;
import com.yongyongwang.multimedia.choose.magic.filter.advanced.MagicBrannanFilter;
import com.yongyongwang.multimedia.choose.magic.filter.advanced.MagicBrooklynFilter;
import com.yongyongwang.multimedia.choose.magic.filter.advanced.MagicCalmFilter;
import com.yongyongwang.multimedia.choose.magic.filter.advanced.MagicCoolFilter;
import com.yongyongwang.multimedia.choose.magic.filter.advanced.MagicCrayonFilter;
import com.yongyongwang.multimedia.choose.magic.filter.advanced.MagicEarlyBirdFilter;
import com.yongyongwang.multimedia.choose.magic.filter.advanced.MagicEmeraldFilter;
import com.yongyongwang.multimedia.choose.magic.filter.advanced.MagicEvergreenFilter;
import com.yongyongwang.multimedia.choose.magic.filter.advanced.MagicFairytaleFilter;
import com.yongyongwang.multimedia.choose.magic.filter.advanced.MagicFreudFilter;
import com.yongyongwang.multimedia.choose.magic.filter.advanced.MagicHealthyFilter;
import com.yongyongwang.multimedia.choose.magic.filter.advanced.MagicHefeFilter;
import com.yongyongwang.multimedia.choose.magic.filter.advanced.MagicHudsonFilter;
import com.yongyongwang.multimedia.choose.magic.filter.advanced.MagicImageAdjustFilter;
import com.yongyongwang.multimedia.choose.magic.filter.advanced.MagicInkwellFilter;
import com.yongyongwang.multimedia.choose.magic.filter.advanced.MagicKevinFilter;
import com.yongyongwang.multimedia.choose.magic.filter.advanced.MagicLatteFilter;
import com.yongyongwang.multimedia.choose.magic.filter.advanced.MagicLomoFilter;
import com.yongyongwang.multimedia.choose.magic.filter.advanced.MagicN1977Filter;
import com.yongyongwang.multimedia.choose.magic.filter.advanced.MagicNashvilleFilter;
import com.yongyongwang.multimedia.choose.magic.filter.advanced.MagicNostalgiaFilter;
import com.yongyongwang.multimedia.choose.magic.filter.advanced.MagicPixarFilter;
import com.yongyongwang.multimedia.choose.magic.filter.advanced.MagicRiseFilter;
import com.yongyongwang.multimedia.choose.magic.filter.advanced.MagicRomanceFilter;
import com.yongyongwang.multimedia.choose.magic.filter.advanced.MagicSakuraFilter;
import com.yongyongwang.multimedia.choose.magic.filter.advanced.MagicSierraFilter;
import com.yongyongwang.multimedia.choose.magic.filter.advanced.MagicSketchFilter;
import com.yongyongwang.multimedia.choose.magic.filter.advanced.MagicSkinWhitenFilter;
import com.yongyongwang.multimedia.choose.magic.filter.advanced.MagicSunriseFilter;
import com.yongyongwang.multimedia.choose.magic.filter.advanced.MagicSunsetFilter;
import com.yongyongwang.multimedia.choose.magic.filter.advanced.MagicSutroFilter;
import com.yongyongwang.multimedia.choose.magic.filter.advanced.MagicSweetsFilter;
import com.yongyongwang.multimedia.choose.magic.filter.advanced.MagicTenderFilter;
import com.yongyongwang.multimedia.choose.magic.filter.advanced.MagicToasterFilter;
import com.yongyongwang.multimedia.choose.magic.filter.advanced.MagicValenciaFilter;
import com.yongyongwang.multimedia.choose.magic.filter.advanced.MagicWaldenFilter;
import com.yongyongwang.multimedia.choose.magic.filter.advanced.MagicWarmFilter;
import com.yongyongwang.multimedia.choose.magic.filter.advanced.MagicWhiteCatFilter;
import com.yongyongwang.multimedia.choose.magic.filter.advanced.MagicXproIIFilter;
import com.yongyongwang.multimedia.choose.magic.filter.base.gpuimage.GPUImageBrightnessFilter;
import com.yongyongwang.multimedia.choose.magic.filter.base.gpuimage.GPUImageContrastFilter;
import com.yongyongwang.multimedia.choose.magic.filter.base.gpuimage.GPUImageExposureFilter;
import com.yongyongwang.multimedia.choose.magic.filter.base.gpuimage.GPUImageFilter;
import com.yongyongwang.multimedia.choose.magic.filter.base.gpuimage.GPUImageHueFilter;
import com.yongyongwang.multimedia.choose.magic.filter.base.gpuimage.GPUImageSaturationFilter;
import com.yongyongwang.multimedia.choose.magic.filter.base.gpuimage.GPUImageSharpenFilter;

public class MagicFilterFactory{
	
	private static MagicFilterType filterType = MagicFilterType.NONE;
	
	public static GPUImageFilter initFilters(MagicFilterType type){
		filterType = type;
		switch (type) {
		case WHITECAT:
			return new MagicWhiteCatFilter();
		case BLACKCAT:
			return new MagicBlackCatFilter();
		case SKINWHITEN:
			return new MagicSkinWhitenFilter();
		case ROMANCE:
			return new MagicRomanceFilter();
		case SAKURA:
			return new MagicSakuraFilter();
		case AMARO:
			return new MagicAmaroFilter();
		case WALDEN:
			return new MagicWaldenFilter();
		case ANTIQUE:
			return new MagicAntiqueFilter();
		case CALM:
			return new MagicCalmFilter();
		case BRANNAN:
			return new MagicBrannanFilter();
		case BROOKLYN:
			return new MagicBrooklynFilter();
		case EARLYBIRD:
			return new MagicEarlyBirdFilter();
		case FREUD:
			return new MagicFreudFilter();
		case HEFE:
			return new MagicHefeFilter();
		case HUDSON:
			return new MagicHudsonFilter();
		case INKWELL:
			return new MagicInkwellFilter();
		case KEVIN:
			return new MagicKevinFilter();
		case LOMO:
			return new MagicLomoFilter();
		case N1977:
			return new MagicN1977Filter();
		case NASHVILLE:
			return new MagicNashvilleFilter();
		case PIXAR:
			return new MagicPixarFilter();
		case RISE:
			return new MagicRiseFilter();
		case SIERRA:
			return new MagicSierraFilter();
		case SUTRO:
			return new MagicSutroFilter();
		case TOASTER2:
			return new MagicToasterFilter();
		case VALENCIA:
			return new MagicValenciaFilter();
		case XPROII:
			return new MagicXproIIFilter();
		case EVERGREEN:
			return new MagicEvergreenFilter();
		case HEALTHY:
			return new MagicHealthyFilter();
		case COOL:
			return new MagicCoolFilter();
		case EMERALD:
			return new MagicEmeraldFilter();
		case LATTE:
			return new MagicLatteFilter();
		case WARM:
			return new MagicWarmFilter();
		case TENDER:
			return new MagicTenderFilter();
		case SWEETS:
			return new MagicSweetsFilter();
		case NOSTALGIA:
			return new MagicNostalgiaFilter();
		case FAIRYTALE:
			return new MagicFairytaleFilter();
		case SUNRISE:
			return new MagicSunriseFilter();
		case SUNSET:
			return new MagicSunsetFilter();
		case CRAYON:
			return new MagicCrayonFilter();
		case SKETCH:
			return new MagicSketchFilter();
		//image adjust
		case BRIGHTNESS:
			return new GPUImageBrightnessFilter();
		case CONTRAST:
			return new GPUImageContrastFilter();
		case EXPOSURE:
			return new GPUImageExposureFilter();
		case HUE:
			return new GPUImageHueFilter();
		case SATURATION:
			return new GPUImageSaturationFilter();
		case SHARPEN:
			return new GPUImageSharpenFilter();
		case IMAGE_ADJUST:
			return new MagicImageAdjustFilter();
		default:
			return null;
		}
	}
	
	public MagicFilterType getCurrentFilterType(){
		return filterType;
	}
}
