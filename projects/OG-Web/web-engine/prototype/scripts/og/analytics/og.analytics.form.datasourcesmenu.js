/*
 * Copyright 2012 - present by OpenGamma Inc. and the OpenGamma group of companies
 * Please see distribution for license.
 */
$.register_module({
    name: 'og.analytics.form.DatasourcesMenu',
    dependencies: [],
    obj: function () {
        var module = this, Block = og.common.util.ui.Block;
        var DatasourcesMenu = function (config) {
            if (!config) return og.dev.warn('og.analytics.DatasourcesMenu: Missing param [config] to constructor.');

            if (!(config.hasOwnProperty('form')) || !(config.form instanceof og.common.util.ui.Form))
                return og.dev.warn('og.analytics.DatasourcesMenu: Missing param key [config.form] to constructor.');

            // Private
            var menu, query = [], resolver_keys = [], snapshots = {}, $query, default_type_txt = 'select type...',
                default_sel_txt = 'select data source...', del_s = '.og-icon-delete', parent_s = '.OG-dropmenu-options',
                wrapper = '<wrapper>', type_s = '.type', source_s = '.source',  extra_opts_s = '.extra-opts',
                latest_s = '.latest', custom_s = '.custom', custom_val = 'Custom', date_selected_s = 'date-selected',
                active_s = 'active', versions_s = '.versions', corrections_s = '.corrections', initialized = false,
                default_datasource = {
                    type:'Live',
                    source:'Bloomberg',
                    datasource: 'livedatasources', //other sources [marketdatasnapshots, configs]
                    api_opts:{cache_for:5000} //other opts [{type: 'HistoricalTimeSeriesRating'}]
                },
                form = config.form, datasources = config.datasources || [default_datasource];

            var add_handler = function (obj) {
                add_row_handler(obj).html(function (html) {
                    menu.add_handler($(html));
                    source_handler(obj.pos);
                });
            };

            var add_row_handler = function (obj) {
                return new form.Block({
                    module: 'og.analytics.form_datasources_row_tash',
                    extras: {
                        types: ['Live', 'Snapshot', 'Historical'].map(function (entry) {
                            return {text: entry, selected: obj.type === entry};
                        })
                    },
                    children: [add_source_dropdown(obj)]
                });
            };

            var add_source_dropdown = function (obj) {
                var datasource = obj.datasource.split('.').reduce(function (api, key) {return api[key];}, og.api.rest);
                return new form.Block({
                    module: 'og.analytics.form_datasources_source_tash',
                    generator: function (handler, tmpl, data) {
                        datasource.get(obj.api_opts).pipe(function (resp) {
                        if (resp.error) return og.dev.warn('og.analytics.DatasourcesMenu: ' + resp.error);
                            data.source = resp.data.map(function (entry) {
                                return {text: entry, selected: obj.source === entry};
                            });
                            handler(tmpl(data));
                        });
                    }
                });
            };

            var date_handler = function (entry, preload) { // TODO AG: refocus custom, hide datepicker
                if (!menu.opts[entry]) return;
                var custom = $(custom_s, menu.opts[entry]), latest = $(latest_s, menu.opts[entry]),
                    idx = query.pluck('pos').indexOf(menu.opts[entry].data('pos'));
                if (custom) custom.addClass(active_s+ ' ' +date_selected_s);
                if (latest) latest.removeClass(active_s);
                if (preload && 'date' in preload) custom.val(preload.date);
                if (custom.parent().is(versions_s)) query[idx].version_date = custom.datepicker('getDate');
                else if (custom.parent().is(corrections_s)) query[idx].correction_date = custom.val();
                else query[idx].date = custom.val();
            };

            var delete_handler = function (entry) {
                if (!menu.opts[entry]) return;
                if (menu.opts.length === 1 && query.length) return remove_ext_opts(entry), reset_query(entry);
                var idx = query.pluck('pos').indexOf(menu.opts[entry].data('pos')),
                    sel_pos = menu.opts[entry].data('pos');
                menu.delete_handler(menu.opts[entry]);
                if (menu.opts.length) {
                    for (var i = ~idx ? idx : sel_pos, len = query.length; i < len; query[i++].pos -= 1);
                    if (~idx) return remove_entry(idx), display_query(); // fire; optsrepositioned
                }
            };

            var display_datepicker = function (entry) {
                if (!menu.opts[entry]) return;
                var custom_dp = $(custom_s, menu.opts[entry]), widget;
                custom_dp.datepicker({
                    dateFormat:'yy-mm-dd',
                    onSelect: function () {
                        date_handler(entry);
                        widget.off(events.preventblurkill);
                    }
                })
                .datepicker('show');
                widget = custom_dp.datepicker('widget').on(events.preventblurkill, function(event) {
                    menu.fire(events.open);
                });
            };

            var display_query = function () {
                if(query.length) {
                    var i = 0, arr = [];
                    query.sort(menu.sort_opts).forEach(function (entry) { // revisit the need for sorting this..
                        if (i > 0) arr[i++] = menu.$dom.toggle_infix.html() + " ";
                        arr[i++] = entry;
                    });
                    $query.html(arr.reduce(function (a, v) {
                        return a += v.type ? menu.capitalize(v.type) + ":" + v.src : menu.capitalize(v);
                    }, ''));
                } else $query.text(default_sel_txt);
            };

            var enable_extra_options = function (entry, val) {
                if (!menu.opts[entry]) return;
                var inputs = $(extra_opts_s, menu.opts[entry]).find('input');
                if (!inputs) return;
                if (val) inputs.removeAttr('disabled').filter(latest_s).addClass(active_s);
                else inputs.attr('disabled', true).filter('.'+active_s).removeClass(active_s);
                inputs.filter(custom_s).removeClass(active_s+ ' ' +date_selected_s).val(custom_val);
            };

            var get_query = function () {
                if (!query.length) return;
                var arr = [];
                query.forEach(function (entry) {
                    var obj = {}, val = entry.type.toLowerCase();
                    switch (val) {
                        case 'live': obj['marketDataType'] = val, obj['source'] = entry.src; break;
                        case 'snapshot': obj['marketDataType'] = val, obj['snapshotId'] = snapshots[entry.src]; break;
                        case 'historical':
                            if (entry.date) {
                                obj['marketDataType'] = 'fixedHistorical';
                                obj['date'] = entry.date;
                            } else obj['marketDataType'] = 'latestHistorical';
                            obj['resolverKey'] = entry.src; break;
                        //no default
                    }
                    arr.push(obj);
                });
                return arr;
            };

            var get_snapshot = function (id) {
                if (!id) return;
                return Object.keys(snapshots).filter(function(key) {
                    return snapshots[key] === id;
                })[0];
            };

            var init = function (config) {
                menu = new og.analytics.form.DropMenu({cntr: $('.OG-analytics-form .og-datasources')});
                if (menu.$dom) {
                    $query = $('.datasources-query', menu.$dom.toggle);
                    if (menu.$dom.menu)
                        menu.$dom.menu.on('click', 'input, button, div.og-icon-delete, a.OG-link-add', menu_handler)
                            .on('change', 'select', menu_handler);
                    if (datasources.length === 1) {
                        $('.OG-dropmenu-options', menu.$dom.menu).data('pos', 0);
                        source_handler(0);
                    }
                    menu.fire('initialized', [initialized = true]);
                }
            };

            var menu_handler = function (event) {
                var entry, elem = $(event.srcElement || event.target), parent = elem.parents(parent_s),
                    source = default_datasource;
                if (!parent) return;
                entry = parent.data('pos');
                source.pos = menu.opts.length;
                if (elem.is(menu.$dom.add)) return menu.stop(event), add_handler(source);
                if (elem.is(del_s)) return menu.stop(event), delete_handler(entry);
                if (elem.is(type_s)) return type_handler(entry);
                if (elem.is(source_s)) return source_handler(entry);
                if (elem.is(custom_s)) return display_datepicker(entry);
                if (elem.is(latest_s)) return remove_date(entry);
                if (elem.is('button')) return menu.button_handler(elem.text());
            };

            var populate_src_options = function (entry, data) {
                if (!menu.opts[entry]) return;
                var source_select = $(source_s, menu.opts[entry]),
                    type_val = $(type_s, menu.opts[entry]).val().toLowerCase();
                if (!source_select) return;
                if (type_val) menu.opts[entry].data('type', type_val).addClass(type_val);
                source_select.hide();
                (data || []).forEach(function (d) {
                    if ($.isPlainObject(d) && 'name' in d && typeof d.name === 'string') snapshots[d.name] = d.id;
                    // source_select.append($($option.html()).text(d.name || d));
                });
                source_select.show();
            };

            var remove_date = function (entry) {
                if (!menu.opts[entry]) return;
                var custom = $(custom_s, menu.opts[entry]).removeClass(active_s+ ' ' +date_selected_s).val(custom_val),
                    latest = $(latest_s, menu.opts[entry]).addClass(active_s);
                if (custom.parent().is(versions_s)) delete query[entry].version_date;
                else if (custom.parent().is(corrections_s)) delete query[entry].correction_date;
                else delete query[entry].date;
            };

            var remove_entry = function (entry) {
                if (menu.opts.length === 1 && query.length === 1) return query.length = 0; // fire; resetquery
                if (~entry) query.splice(entry, 1);
            };

            var remove_ext_opts = function (entry) {
                if (!menu.opts[entry]) return;
                var parent = menu.opts[entry];
                return reset_source_select(entry), parent.removeClass(parent.data('type')).find(extra_opts_s).remove();
            };

            var remove_orphans = function () {
                for (var i = menu.opts.length-1; i >= 0; i-=1){
                    if (menu.opts.length === 1) break;
                    var option = menu.opts[i];
                    if ($(type_s, option).val() === default_type_txt || $(source_s, option).val() === default_sel_txt)
                        menu.delete_handler(option);
                }
            };

            var reset_query = function (entry) {
                if (!menu.opts[entry]) return;
                var type_select = $(type_s, menu.opts[entry]);
                return $query.text(default_sel_txt), type_select.val(default_sel_txt).focus(), remove_entry();
            };

            var reset_source_select = function (entry) {
                if (!menu.opts[entry]) return;
                var source_select = $(source_s, menu.opts[entry]).hide(), options = $('option', source_select), i;
                if (!source_select || !options.length) return;
                options.each(function(idx){ // IE
                    if (idx > 0) $(this).remove();
                });
                source_select.show();
            };

            var source_handler = function (entry) {
                if (!menu.opts[entry]) return;
                var val, src, option, sel_pos = menu.opts[entry].data('pos'),
                    type_val = $(type_s, menu.opts[entry]).val().toLowerCase(),
                    source_select = $(source_s, menu.opts[entry]),
                    source_val = source_select.val(),
                    idx = query.pluck('pos').indexOf(sel_pos);
                if (source_val === default_sel_txt) {
                    return remove_entry(idx), display_query(), enable_extra_options(entry, false);
                } else if (~idx) query[idx] = {pos:sel_pos, type:type_val, src:source_val};
                else query.splice(sel_pos, 0, {pos:sel_pos, type:type_val, src:source_val});
                enable_extra_options(entry, true);
                display_query();
            };

            var type_handler = function (entry, conf) {
                if (!menu.opts[entry]) return;
                var parent = menu.opts[entry], type_select = $(type_s, parent),
                    type_val = type_select.val().toLowerCase(), idx = query.pluck('pos').indexOf(parent.data('pos'));
                if (type_val === default_type_txt.toLowerCase()){
                    if (menu.opts.length === 1 && query.length === 1) return remove_ext_opts(entry), reset_query(entry);
                    return remove_entry(idx), remove_ext_opts(entry), display_query(),
                        enable_extra_options(entry, false);
                }
                if (parent.hasClass(parent.data('type'))) {
                    remove_entry(idx); remove_ext_opts(entry); display_query();
                }
            };

            form.Block.call(this, {
                data: { providers: [] },
                selector: '.og-datasources',
                module: 'og.analytics.form_datasources_tash',
                children: datasources.map(add_row_handler),
                processor: function (data) {
                    data.providers = get_query();
                }
            });

            form.on('form:load', init);
        };

        DatasourcesMenu.prototype = new Block;

        return DatasourcesMenu;
    }
});