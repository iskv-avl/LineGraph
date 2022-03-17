package co.iskv.crypto.widget

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import co.iskv.crypto.R
import co.iskv.crypto.widget.models.*
import co.iskv.crypto.widget.models.Point

class LineGraphView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    //Прозрачность для графика и точки
    private val graphAlpha = 170

    //region Paint

    //Для горизонтальных разделителей (сетки)
    private val separatorsPaint = Paint().apply {
        style = Paint.Style.FILL
        color = ContextCompat.getColor(context, R.color.color_light_gray)
    }

    //Для background выбранного периода
    private val periodPaint = Paint().apply {
        style = Paint.Style.FILL
        color = ContextCompat.getColor(context, R.color.color_gray)
    }

    //Для модального окошка выбранной точки
    private val modalPaint = Paint().apply {
        style = Paint.Style.FILL
        pathEffect = CornerPathEffect(resources.getDimension(R.dimen._8dp))
        color = ContextCompat.getColor(context, R.color.modal_color)
    }

    //Для линии графика
    private val graphPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        pathEffect = CornerPathEffect(resources.getDimensionPixelSize(R.dimen._8dp).toFloat())
        style = Paint.Style.STROKE
        strokeWidth = resources.getDimensionPixelSize(R.dimen._2dp).toFloat()
        color = ContextCompat.getColor(context, R.color.color_red)
        alpha = graphAlpha
    }

    //Для выбранной точки на графике
    private val graphSelectedPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        strokeWidth = resources.getDimensionPixelSize(R.dimen._2dp).toFloat()
        color = ContextCompat.getColor(context, R.color.color_white)
        alpha = graphAlpha
    }

    //Для текста в названии валют
    private val coinNamePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        typeface = ResourcesCompat.getFont(context, R.font.poppins_medium)
        textSize = resources.getDimension(R.dimen._18sp)
        color = ContextCompat.getColor(context, R.color.color_black)
    }

    //Для текста в периодах
    private val periodNamePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        typeface = ResourcesCompat.getFont(context, R.font.poppins)
        textSize = resources.getDimension(R.dimen._14sp)
        color = ContextCompat.getColor(context, R.color.color_black)
    }

    //Для текста в легенде
    private val peakValuePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        typeface = ResourcesCompat.getFont(context, R.font.poppins)
        textSize = resources.getDimension(R.dimen._12sp)
        color = ContextCompat.getColor(context, R.color.color_middle_gray)
    }

    //Для текста в модалке
    private val modalValuePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textAlign = Paint.Align.CENTER
        typeface = ResourcesCompat.getFont(context, R.font.poppins_medium)
        textSize = resources.getDimension(R.dimen._12sp)
        color = ContextCompat.getColor(context, R.color.color_black)
    }
    //endregion

    //region Размеры для хедера и футора
    private val peakBottomMargin = resources.getDimension(R.dimen._2dp)
    private val headerMargin = resources.getDimension(R.dimen._16dp)
    private val footerMargin = resources.getDimension(R.dimen._16dp)
    private val footerBottomMargin = resources.getDimension(R.dimen._16dp)
    private val headerBetweenItemsMargin =
        resources.getDimension(R.dimen._10dp)
    private val headerHeight = resources.getDimension(R.dimen._40dp)
    private val rowHeight = resources.getDimensionPixelSize(R.dimen._48dp)
    //endregion

    //region Размеры для точки на графике
    private val pointTouchRadius = resources.getDimension(R.dimen._12dp)
    private val pointRadius = resources.getDimension(R.dimen._6dp)
    //endregion

    //region Размеры для периода
    private val periodRadius = resources.getDimension(R.dimen._20dp)
    private val periodMarginHeight = resources.getDimension(R.dimen._16dp)
    private val periodMarginTopHeight = resources.getDimension(R.dimen._20dp)
    private val periodHeight = resources.getDimension(R.dimen._30dp)
    private val periodWidth
        get() = (measuredWidth - footerMargin * 2 - ((periods.size - 1) * periodMarginHeight)) / periods.size
    //endregion

    //region Модалка
    private val rectModal: RectF = RectF()
    private val modalHeight = resources.getDimension(R.dimen._52dp)
    private val pointToModalMargin = resources.getDimension(R.dimen._8dp)
    private val horizontalModalPadding = resources.getDimension(R.dimen._12dp)
    private val verticalModalPadding = resources.getDimension(R.dimen._4dp)
    private val triangleModelHeight = resources.getDimension(R.dimen._24dp)
    //endregion

    private val separatorHeight = resources.getDimension(R.dimen._1dp)
    private val separatorMarginStart = resources.getDimension(R.dimen._16dp)

    private var coinData: CoinModel? = null

    private val graphStartY = headerHeight + rowHeight
    private val graphEndY
        get() = rowHeight * (coinData?.legend?.size ?: 1) + headerHeight

    private val rowRect = Rect()
    private var path = Path()
    private val modalPath = Path()

    private val contentHeight
        get() = rowHeight * (coinData?.legend?.size
            ?: 1) + headerHeight + footerBottomMargin + periodMarginTopHeight + periodHeight

    private val contentHeightWithoutFooter
        get() = rowHeight * (coinData?.legend?.size ?: 1) + headerHeight + footerBottomMargin


    private var selectedItemPeriod = 0
    private var graphPoints = mutableListOf<Point>()
    private var selectedGraphPoint: Int = -1
    private var periods: Array<String> = resources.getStringArray(R.array.periods_array)

    private var isVisiblePeriodSwitcher: Boolean
    private var isVisibleGrid: Boolean
    private var isVisibleLegend: Boolean
    private var periodDrawable: Drawable? = null
    private var customBackground: Drawable? = null

    // Значения последнего эвента
    private val lastPoint = PointF()
    private var lastPointerId = 0

    private var onPeriodClickListener: ((Period) -> Unit)? = null

    private val currentGraphColor: Int
        get() = ContextCompat.getColor(
            context, when (coinData!!.percent.direction) {
                Direction.UP -> R.color.color_green
                Direction.FALL -> R.color.color_red
            }
        )

    init {
        context.obtainStyledAttributes(attrs, R.styleable.LineGraphView, 0, 0).apply {
            isVisiblePeriodSwitcher =
                getBoolean(R.styleable.LineGraphView_isVisiblePeriodSwitcher, true)
            isVisibleGrid = getBoolean(R.styleable.LineGraphView_isVisibleGrid, true)
            isVisibleLegend = getBoolean(R.styleable.LineGraphView_isVisibleLegend, true)
            periodDrawable = getDrawable(R.styleable.LineGraphView_periodDrawable)
            periodDrawable = getDrawable(R.styleable.LineGraphView_periodDrawable)
            customBackground = getDrawable(R.styleable.LineGraphView_background)
                ?: ContextCompat.getDrawable(context, R.drawable.bg_white_around_16dp)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val heightSpecSize = MeasureSpec.getSize(heightMeasureSpec)
        val height = when (MeasureSpec.getMode(heightMeasureSpec)) {
            MeasureSpec.UNSPECIFIED -> contentHeight.toInt()
            MeasureSpec.EXACTLY -> heightSpecSize
            MeasureSpec.AT_MOST -> heightSpecSize.coerceAtMost(heightSpecSize)
            else -> error("Unreachable")
        }
        setMeasuredDimension(width, height)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        rowRect.set(0, 0, w, rowHeight)
    }

    override fun onDraw(canvas: Canvas) {
        with(canvas) {
            drawHeader()
            drawRow()
            if (isVisiblePeriodSwitcher) drawFooter()
            drawGraph()
            background = customBackground

        }
    }

    //Сохраняем состояния
    override fun onSaveInstanceState(): Parcelable {
        return SavedState(super.onSaveInstanceState()).also {
            it.selectedPeriodItem = selectedItemPeriod
            it.selectedGraphPoint = selectedGraphPoint
        }
    }

    //Востанавливаем состояния
    override fun onRestoreInstanceState(state: Parcelable?) {
        if (state is SavedState) {
            super.onRestoreInstanceState(state.superState)
            selectedItemPeriod = state.selectedPeriodItem
            selectedGraphPoint = state.selectedGraphPoint
        } else {
            super.onRestoreInstanceState(state)
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event ?: return false
        if (event.pointerCount != 1) return false
        when (event.action) {
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                //region Отлавливаем нажитие по периудам
                if (event.getY(0) in contentHeightWithoutFooter..contentHeightWithoutFooter + periodHeight) {
                    val itemWidth = periodWidth
                    periods.forEachIndexed { index, _ ->
                        val periodStart =
                            (itemWidth * index + periodMarginHeight.toFloat() * (index + 1))
                        val periodEnd = periodStart + itemWidth
                        if (event.getX(0) in periodStart..periodEnd) {
                            onPeriodClickListener?.invoke(Period.values()[index])
                            selectedGraphPoint = -1
                            selectedItemPeriod = index
                            invalidate()
                            return true
                        }
                    }
                }
                //endregion

                //region Отлавливаем выбор точки на графике
                if (event.getY(0) in graphStartY..graphEndY) {
                    graphPoints.indexOfFirst { point ->
                        event.getY(0) in (point.y - pointTouchRadius / 2)..((point.y + pointTouchRadius / 2))
                                && event.getX(0) in (point.x - pointTouchRadius / 2)..((point.x + pointTouchRadius / 2))
                    }.let {
                        selectedGraphPoint = it
                        modalPath.reset()
                        invalidate()
                        return true
                    }
                }
                //endregion
            }

            //region Отлавливаем скролл по графику
            MotionEvent.ACTION_MOVE -> {
                val pointerId = event.getPointerId(0)

                // Чтобы избежать скачков - сдвигаем, только если поинтер тот же, что и раньше
                if (lastPointerId == pointerId) {
                    graphPoints.forEachIndexed { index, point ->
                        if (event.getX(0) < point.x) {
                            selectedGraphPoint = index
                            modalPath.reset()
                            invalidate()
                            return true
                        }
                    }
                }
                // Запоминаем поинтер и последнюю точку в любом случае
                lastPoint.set(event.x, event.y)
                lastPointerId = event.getPointerId(0)
                return true
            }
            //endregion
        }
        return true
    }

    fun setGraphData(period: Period, model: CoinModel) {
        if (coinData != null) {
            selectedGraphPoint = -1
        }
        selectedItemPeriod = period.ordinal
        coinData = model
        graphPoints.clear()
        updateGraph()
    }

    fun setOnPeriodClickListener(listener: (Period) -> Unit) {
        onPeriodClickListener = listener
    }

    private fun Canvas.drawHeader() {
        //region Отрисовка текущей цены
        coinData ?: return
        val currentPrice = coinData!!.getCurrentPrice().name
        val currentPriceX = measuredWidth - periodNamePaint.measureText(currentPrice) - headerMargin
        val currentPriceY = periodNamePaint.getTextBottom(headerHeight)
        //Рисуем цену за единицу валюты на данный момент
        drawText(
            currentPrice,
            currentPriceX,
            currentPriceY,
            periodNamePaint.apply {
                this.color = ContextCompat.getColor(context, R.color.color_black)
            })
        //endregion

        //region Отрисовка процентов
        val percentValue = coinData!!.percent.percent
        val color = when (coinData!!.percent.direction) {
            Direction.UP -> R.color.color_green
            Direction.FALL -> R.color.color_red
        }
        val percentValueX =
            currentPriceX - periodNamePaint.measureText(percentValue) - headerBetweenItemsMargin
        val percentValueY = periodNamePaint.getTextBottom(headerHeight)
        //Рисуем разницу с предыдущими показаниями в процентах
        drawText(
            percentValue,
            percentValueX,
            percentValueY,
            periodNamePaint.apply { this.color = ContextCompat.getColor(context, color) })
        //endregion

        //region Отрисовка названия валюты
        val coinName = coinData!!.name
        val coinNameX = headerMargin
        val coinNameY = coinNamePaint.getTextBottom(headerHeight)
        drawText(coinName, coinNameX, coinNameY, coinNamePaint)
        //endregion
    }

    private fun updateGraph() {
        path.reset()
        modalPath.reset()
        graphPoints.clear()
        invalidate()
    }

    private fun Canvas.drawFooter() {
        coinData ?: return
        val itemWidth = periodWidth
        val periodX = (coinData!!.legend.size * rowHeight) + headerHeight + periodMarginTopHeight

        periods.forEachIndexed { index, period ->
            val periodStart = (itemWidth * index + periodMarginHeight * (index + 1))
            if (index == selectedItemPeriod) {

                if (periodDrawable != null) {
                    periodDrawable!!.setBounds(
                        periodStart.toInt(),
                        periodX.toInt(),
                        (periodStart + itemWidth).toInt(),
                        (periodX + periodHeight).toInt()
                    )
                    periodDrawable!!.draw(this)
                } else {
                    drawRoundRect(
                        periodStart,
                        periodX,
                        periodStart + itemWidth,
                        periodX + periodHeight,
                        periodRadius,
                        periodRadius,
                        periodPaint
                    )
                }

                periodNamePaint.apply {
                    color = ContextCompat.getColor(context, R.color.color_black)
                }
            } else {
                periodNamePaint.apply {
                    color = ContextCompat.getColor(context, R.color.color_dark_gray)
                }
            }
            val periodNameX = periodNamePaint.measureText(period)
            val periodNameY =
                periodNamePaint.getTextBaselineByCenter(periodX + (periodHeight / 2))
            drawText(
                period,
                periodStart + (itemWidth / 2) - (periodNameX / 2),
                periodNameY,
                periodNamePaint
            )
        }
    }

    private fun Canvas.drawRow() {
        coinData ?: return

        //Сдвиг от левой границы View
        val nameX = separatorMarginStart.toFloat()
        //Перебираем набор значений легенды
        coinData!!.legend.forEachIndexed { index, peakName ->

            //Отрисовка вертикальной легенды
            if (isVisibleLegend) {
                val nameY =
                    headerHeight + (rowHeight.toFloat() * index) + peakValuePaint.getTextBottom(
                        rowHeight.toFloat()
                    ) - peakBottomMargin
                drawText(peakName, nameX, nameY, peakValuePaint)
            }

            //Отрисовка горизонтальных линий (сетки)
            if (isVisibleGrid) {
                rowRect.offsetTo(0, rowHeight * index)
                drawLine(
                    nameX,
                    headerHeight + (rowHeight.toFloat() * (index + 1) - separatorHeight / 2),
                    width.toFloat(),
                    headerHeight + (rowHeight.toFloat() * (index + 1) + separatorHeight / 2),
                    separatorsPaint
                )
            }
        }
    }

    private fun Canvas.drawModal(pointName: String, date: String, selectedPoint: Point) {
        val graphEndX = measuredWidth

        val nameWidth = modalValuePaint.measureText(pointName)
        val dateWidth = modalValuePaint.measureText(date)
        //вычисляем максимальную ширину модалки
        val modalWidth =
            (if (nameWidth < dateWidth) dateWidth else nameWidth) + horizontalModalPadding * 2

        //вычисляем сдвиги справа и слева, если модалка выходит за края границ view
        val startBias = (selectedPoint.x - modalWidth / 2).takeIf { it < 0f } ?: 0f
        val endBias = (selectedPoint.x + modalWidth / 2 - graphEndX).takeIf { it > 0f } ?: 0f

        //вычисляем сдвиг, чтобы переместить контент над выбранной точкой
        val heightOffset = (triangleModelHeight / 2) + pointToModalMargin

        //вычисляем y координату, для сдвига стоимости над точкой
        val nameY =
            modalValuePaint.getTextBaselineByCenter((modalHeight / 2) + verticalModalPadding - (modalHeight / 4)) + selectedPoint.y - modalHeight - heightOffset

        //вычисляем y координату, для сдвига даты над точкой
        val dateY =
            modalValuePaint.getTextBaselineByCenter((modalHeight / 2) - verticalModalPadding + (modalHeight / 4)) + selectedPoint.y - modalHeight - heightOffset
        rectModal.set(0f, 0f, modalWidth, modalHeight)

        //вычисляем сдвиг модалки
        rectModal.offset(
            selectedPoint.x - startBias - endBias - (modalWidth / 2),
            selectedPoint.y - modalHeight - heightOffset
        )

        //вычисляем указатель(треугольный)
        if (modalPath.isEmpty) {
            modalPath.moveTo(selectedPoint.x, selectedPoint.y - pointToModalMargin)
            modalPath.lineTo(
                selectedPoint.x - (triangleModelHeight / 2),
                selectedPoint.y - (triangleModelHeight / 2) - pointToModalMargin
            )
            modalPath.lineTo(
                selectedPoint.x + (triangleModelHeight / 2),
                selectedPoint.y - (triangleModelHeight / 2) - pointToModalMargin
            )
            modalPath.close()
        }

        //рисуем указатель
        modalPaint.apply {
            pathEffect = CornerPathEffect(resources.getDimension(R.dimen._2dp))
        }
        drawPath(modalPath, modalPaint)

        //рисуем модалку
        modalPaint.apply {
            pathEffect = CornerPathEffect(resources.getDimension(R.dimen._8dp))
        }
        drawRect(rectModal, modalPaint)

        //рисуем текст (со сдвигами если он выходит за границы view)
        modalValuePaint.apply { color = ContextCompat.getColor(context, R.color.color_black) }
        drawText(pointName, selectedPoint.x - startBias - endBias, nameY, modalValuePaint)
        modalValuePaint.apply { color = ContextCompat.getColor(context, R.color.color_dark_gray) }
        drawText(date, selectedPoint.x - startBias - endBias, dateY, modalValuePaint)
    }

    private fun Canvas.drawGraph() {
        if (coinData != null && coinData!!.values.size < 2) return

        //Задаем нужный цвет для графика
        graphPaint.apply {
            this.color = currentGraphColor
            alpha = graphAlpha
        }

        //Если path и набор точек пусты, то вычисляем их и инициализируем
        if (!path.isEmpty && graphPoints.isNotEmpty()) {

            //Отрисовываем график из path
            drawPath(path, graphPaint)

            //Если у нас есть выбранная точка, то отрисовавыем ее и модалку
            if (selectedGraphPoint != -1) {

                //Получаем координаты точки для отрисовки
                graphPoints.getOrNull(selectedGraphPoint)?.let { point ->

                    //region Отрисовываем белую окружность
                    graphSelectedPaint.apply {
                        color = ContextCompat.getColor(context, R.color.color_white)
                        style = Paint.Style.FILL
                        alpha = 255
                    }
                    drawCircle(point.x, point.y, pointRadius, graphSelectedPaint)
                    //endregion

                    //region Отрисовываем окружность с обводкой в необходимом цвете
                    graphSelectedPaint.apply {
                        color = currentGraphColor
                        style = Paint.Style.STROKE
                        alpha = graphAlpha
                    }
                    drawCircle(point.x, point.y, pointRadius, graphSelectedPaint)
                    //endregion

                    //region Отрисовываем модальное окно с датой и ценой валюты
                    coinData!!.values.get(selectedGraphPoint).let {
                        drawModal(it.name, it.date, point)
                    }
                    //endregion
                }
            }

        } else {
            //Создаем график через path и отрисовываем его

            val periodLength = measuredWidth.toFloat() / (coinData!!.values.size - 1)
            val dependency = (graphEndY - graphStartY) / (coinData!!.max - coinData!!.min)

            coinData!!.values.forEachIndexed { index, value ->
                val y = (graphEndY - (value.value - coinData!!.min) * dependency).toFloat()
                val x = periodLength * index
                if (index == 0) {
                    path.moveTo(x, y)
                } else {
                    path.lineTo(x, y)
                }
                graphPoints.add(Point(x, y))
            }
            drawPath(path, graphPaint)
        }
    }

    private fun Paint.getTextBaselineByCenter(center: Float) = center - (descent() + ascent()) / 2

    private fun Paint.getTextBottom(bottom: Float) =
        bottom - descent()

    private class SavedState : BaseSavedState {
        var selectedPeriodItem: Int = 0
        var selectedGraphPoint: Int = 0

        constructor(superState: Parcelable?) : super(superState)

        constructor(source: Parcel?) : super(source) {
            source?.apply {
                selectedPeriodItem = readInt()
                selectedGraphPoint = readInt()
            }
        }

        override fun writeToParcel(out: Parcel?, flags: Int) {
            super.writeToParcel(out, flags)
            out?.writeInt(selectedPeriodItem)
            out?.writeInt(selectedGraphPoint)
        }

        companion object {
            @JvmField
            val CREATOR: Parcelable.Creator<SavedState> = object : Parcelable.Creator<SavedState> {
                override fun createFromParcel(source: Parcel): SavedState = SavedState(source)
                override fun newArray(size: Int): Array<SavedState?> = arrayOfNulls(size)
            }
        }
    }

}